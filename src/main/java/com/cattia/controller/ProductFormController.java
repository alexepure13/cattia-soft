package com.cattia.controller;

import com.cattia.dto.*;
import com.cattia.entity.ProductCategoryEnum;
import com.cattia.entity.UnitOfMeasurementEnum;
import com.cattia.service.ProductCategoryService;
import com.cattia.service.ProductService;
import com.cattia.service.UnitOfMeasurementService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class ProductFormController extends BaseController {

    private final ProductCategoryService productCategoryService;
    private final UnitOfMeasurementService unitOfMeasurementService;
    private final ProductService productService;

    @GetMapping(value = "/createProduct")
    public ModelAndView createProductForm(@ModelAttribute String formAction, final Model model) {
        ModelAndView mav = new ModelAndView();
        ProductFormDto productForm = new ProductFormDto();
        goToProductForm(FORM_CREATE, productForm, model, mav);
        return mav;
    }

    @PostMapping(value = "/editProduct")
    public ModelAndView editProductForm(@ModelAttribute("productId") Long productId, RedirectAttributes redirectAttributes, final Model model) {
        ModelAndView mav = new ModelAndView();
        ProductFormDto productForm = productService.getProductById(productId);
        if (productForm == null) {
            redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "Product could not be found, ID: " + productId));
            mav.setViewName("redirect:/productOverview");
        } else {
            goToProductForm(FORM_UPDATE, productForm, model, mav);
        }
        return mav;
    }

    @PostMapping(value = "/submitProduct", params = "action=saveProductFormAndGoToOverview")
    public ModelAndView saveProductFormAndGoToOverview(@ModelAttribute("formAction") String formAction, @ModelAttribute("product") @Valid ProductFormDto product, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        ModelAndView mav = new ModelAndView();
        checkIfProductCodeIsUnique(product, bindingResult);
        if (bindingResult.hasErrors()) {
            goToProductForm(formAction, product, model, mav);
        } else {
            if (FORM_CREATE.equals(formAction)) {
                var savedProduct = productService.saveProduct(product, authentication.getName());
                if (savedProduct != null) {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_SUCCESS, "Product '" + product.getCode() + "': " + product.getName() + " was added successfully"));
                } else {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to save a product"));
                }
            } else if (FORM_UPDATE.equals(formAction)) {
                var savedProduct = productService.updateProduct(product);
                if (savedProduct != null) {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_SUCCESS, "Product '" + product.getCode() + "': " + product.getName() + " was was updated successfully"));
                } else {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to update a product"));
                }
            } else {
                redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "Unknown form action"));
            }
            mav.setViewName("redirect:/productOverview");
        }
        return mav;
    }

    private void goToProductForm(String formAction, ProductFormDto product, Model model, ModelAndView mav) {
        List<ProductCategoryEnum> productCategoryList = productCategoryService.getAllActiveProductCategories();
        List<UnitOfMeasurementEnum> unitOfMeasurementList = unitOfMeasurementService.getAllActiveUnitsOfMeasurement();
        model.addAttribute("product", product);
        model.addAttribute("categoryList", productCategoryList);
        model.addAttribute("unitOfMeasurementList", unitOfMeasurementList);
        model.addAttribute("formAction", formAction);
        mav.setViewName("productForm");
    }

    private void checkIfProductCodeIsUnique(ProductFormDto product, BindingResult bindingResult) {
        Map<Long, String> productMap = productService.findByCode(product.getCode());
        //returns a map with key=productId and value=code. Filters out / excludes all the values for the current codeId. If the count of all remaining items is 0 -> code = unique
        boolean isCodeUnique = productMap.entrySet().stream().filter(e -> e.getKey() != product.getId()).count() == 0;
        if (!isCodeUnique) {
            bindingResult.addError(new FieldError("product", "code", product.getCode(), false, null, null, "The product code must be unique (including for deactivated products)"));
        }
    }

    @PostMapping(value = "/submitProduct", params = "action=exitWithoutSavingProductForm")
    public ModelAndView exitWithoutSavingProductForm() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/productOverview");
        return mav;
    }

}
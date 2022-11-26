package com.cattia.controller;

import com.cattia.dto.AlertDTO;
import com.cattia.dto.ProductOverviewDto;
import com.cattia.dto.ProductStockDto;
import com.cattia.entity.StockHistoryActionEnum;
import com.cattia.service.ProductService;
import com.cattia.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductOverviewController extends BaseController {

    private static final String ORDER_ERROR_MESSAGE = "orderErrorMessage";
    private final ProductService productService;
    private final StockHistoryService stockHistoryService;

    @GetMapping(value = "/productOverview")
    public ModelAndView productOverview(@ModelAttribute(ALERT) AlertDTO alert, final Model model) {
        ModelAndView mav = new ModelAndView();
        List<ProductOverviewDto> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);
        model.addAttribute(ALERT, alert);
        mav.setViewName("productOverview");
        return mav;
    }

    @PostMapping(value = "/updateStock")
    public ModelAndView updateStock(@ModelAttribute("productId") Long productId, @ModelAttribute("formAction") StockHistoryActionEnum formAction, final Model model, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        ProductStockDto product = productService.getProductStockById(productId);
        if (product == null) {
            goToProductOverview(redirectAttributes, mav, new AlertDTO(1, ALERT_ERROR, "Product could not be found, ID: " + productId));
        } else if (!List.of(StockHistoryActionEnum.ADD, StockHistoryActionEnum.REMOVED).contains(formAction)) {
            goToProductOverview(redirectAttributes, mav, new AlertDTO(1, ALERT_ERROR, "Invalid form action " + formAction + " for product with ID: " + productId));
        } else {
            model.addAttribute("product", product);
            model.addAttribute("formAction", formAction);
            mav.setViewName("productStockForm");
        }
        return mav;
    }


    @PostMapping(value = "/saveProductStock")
    public ModelAndView updateStock(@ModelAttribute("id") Long productId, @ModelAttribute("formAction") StockHistoryActionEnum formAction, @ModelAttribute("quantity") String quantity, final Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        ModelAndView mav = new ModelAndView();
        if (!List.of(StockHistoryActionEnum.ADD, StockHistoryActionEnum.REMOVED).contains(formAction)) {
            var alert = new AlertDTO(1, ALERT_ERROR, "Invalid form action " + formAction + " for product with ID: " + productId);
            goToProductOverview(redirectAttributes, mav, alert);
            return mav;
        }

        BigDecimal quantityValue;
        try {
            quantityValue = new BigDecimal(quantity);
            if (quantityValue.compareTo(new BigDecimal(0)) <= 0) {
                model.addAttribute(ORDER_ERROR_MESSAGE, "Quantity must be a positive number");
                goToProductStockForm(productId, formAction, model, mav);
                return mav;
            }
            BigDecimal currentStock = productService.getCurrentStock(productId);
            boolean isStockUpdateNegative = currentStock.subtract(quantityValue).compareTo(new BigDecimal(0)) < 0;
            if (List.of(StockHistoryActionEnum.REMOVED, StockHistoryActionEnum.ORDERED).contains(formAction) && isStockUpdateNegative) {
                model.addAttribute(ORDER_ERROR_MESSAGE, "Quantity to withdraw: " + quantityValue + " should not be greater than the current stock: " + currentStock);
                goToProductStockForm(productId, formAction, model, mav);
                return mav;
            }
        } catch (Exception e) {
            goToProductStockForm(productId, formAction, model, mav);
            model.addAttribute(ORDER_ERROR_MESSAGE, "Quantity must be a positive number");
            return mav;
        }

        Long stockHistoryId = stockHistoryService.saveProductHistory(productId, authentication.getName(), formAction, quantityValue);

        if (stockHistoryId > 0) {
            var alert = new AlertDTO(1, ALERT_SUCCESS, "Stock has been update successfully for product: " + productId);
            goToProductOverview(redirectAttributes, mav, alert);
        } else {
            var alert = new AlertDTO(1, ALERT_ERROR, "Product could not be found, ID: " + productId);
            goToProductOverview(redirectAttributes, mav, alert);
        }
        return mav;
    }

    @PostMapping(value = "/deactivateProduct")
    public ModelAndView deactivateProduct(@RequestParam("productId") Long productId, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        AlertDTO alert;
        int result = productService.deactivateProduct(productId);
        if (result != 0) {
            alert = new AlertDTO(1, ALERT_SUCCESS, "The product was deactivated.");
        } else {
            alert = new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to deactivate the product.");
        }
        mav.setViewName("redirect:/productOverview");
        redirectAttributes.addFlashAttribute(ALERT, alert);
        return mav;
    }

    @PostMapping(value = "/activateProduct")
    public ModelAndView activateProduct(@RequestParam("productId") Long productId, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        AlertDTO alert;
        int result = productService.activateProduct(productId);
        if (result != 0) {
            alert = new AlertDTO(1, ALERT_SUCCESS, "The product was activated.");
        } else {
            alert = new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to activate the refugee.");
        }
        mav.setViewName("redirect:/productOverview");
        redirectAttributes.addFlashAttribute(ALERT, alert);
        return mav;
    }

    private void goToProductStockForm(@ModelAttribute("id") Long productId, @ModelAttribute("formAction") StockHistoryActionEnum formAction, Model model, ModelAndView mav) {
        ProductStockDto product = productService.getProductStockById(productId);
        model.addAttribute("product", product);
        model.addAttribute("formAction", formAction);
        mav.setViewName("productStockForm");
    }

    private void goToProductOverview(RedirectAttributes redirectAttributes, ModelAndView mav, AlertDTO alert) {
        redirectAttributes.addFlashAttribute(ALERT, alert);
        mav.setViewName("redirect:/productOverview");
    }



}

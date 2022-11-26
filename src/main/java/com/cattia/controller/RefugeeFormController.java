package com.cattia.controller;

import com.cattia.dto.AlertDTO;
import com.cattia.dto.RefugeeFormDto;
import com.cattia.service.RefugeeService;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RefugeeFormController extends BaseController {

    private final RefugeeService refugeeService;

    @GetMapping(value = "/createRefugee")
    public ModelAndView createProductForm(@ModelAttribute String formAction, final Model model) {
        ModelAndView mav = new ModelAndView();
        RefugeeFormDto refugeeForm = new RefugeeFormDto();
        goToRefugeeForm(FORM_CREATE, refugeeForm, model, mav);
        return mav;
    }

    private void goToRefugeeForm(String formAction, RefugeeFormDto refugee, Model model, ModelAndView mav) {
        model.addAttribute("refugee", refugee);
        model.addAttribute("formAction", formAction);
        mav.setViewName("refugeeForm");
    }

    @PostMapping(value = "/editRefugee")
    public ModelAndView editProductForm(@ModelAttribute("refugeeId") Long refugeeId, final Model model, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        RefugeeFormDto refugeeForm = refugeeService.getRefugeeById(refugeeId);
        if (refugeeForm == null) {
            redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "Product could not be found, ID: " + refugeeId));
            mav.setViewName("redirect:/refugeeOverview");
        } else {
            goToRefugeeForm(FORM_UPDATE, refugeeForm, model, mav);
        }
        return mav;
    }

    @PostMapping(value = "/submitRefugee", params = "action=saveRefugeeFormAndGoToOverview")
    public ModelAndView saveRefugeeFormAndGoToOverview(@ModelAttribute("formAction") String formAction, @ModelAttribute("refugee") @Valid RefugeeFormDto refugee, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        checkIfRefugeeIdentificationNumberIsUnique(refugee, bindingResult);
        if (bindingResult.hasErrors()) {
            goToRefugeeForm(formAction, refugee, model, mav);
        } else {
            if (FORM_CREATE.equals(formAction)) {
                var savedRefugee = refugeeService.saveRefugee(refugee);
                if (savedRefugee != null) {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_SUCCESS, "Refugee '" + refugee.getIdentificationNumber() + "': " + refugee.getFirstName() + " " + refugee.getLastName() + " was added successfully"));
                } else {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to save a refugee"));
                }
            } else if (FORM_UPDATE.equals(formAction)) {
                var savedRefugee = refugeeService.updateRefugee(refugee);
                if (savedRefugee != 0) {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_SUCCESS, "Refugee '" + refugee.getIdentificationNumber() + "': " + refugee.getFirstName() + " " + refugee.getLastName() + " was was updated successfully"));
                } else {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to update a refugee"));
                }
            } else {
                redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "Unknown form action"));
            }
            mav.setViewName("redirect:/refugeeOverview");
        }
        return mav;
    }

    private void checkIfRefugeeIdentificationNumberIsUnique(RefugeeFormDto refugee, BindingResult bindingResult) {
        Map<Long, String> refugeeMap = refugeeService.findByIdentificationNumber(refugee.getIdentificationNumber());
        //returns a map with key=refugeeId and value=identificationNumber. Filters out / excludes all the values for the current identificationNumber. If the count of all remaining items is 0 -> code = unique
        boolean isIdentificationNumberUnique = refugeeMap.entrySet().stream().filter(e -> e.getKey().equals( refugee.getId())).count() == 0;
        if (!isIdentificationNumberUnique) {
            bindingResult.addError(new FieldError("refugee", "identificationNumber", refugee.getIdentificationNumber(), false, null, null, "The identification number must be unique (including for deactivated refugees)"));
        }
    }

    @PostMapping(value = "/submitRefugee", params = "action=exitWithoutSavingRefugeeForm")
    public ModelAndView exitWithoutSavingRefugeeForm() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/refugeeOverview");
        return mav;
    }
}

package com.cattia.controller;

import com.cattia.dto.AlertDTO;
import com.cattia.dto.RefugeeOverviewDto;
import com.cattia.service.RefugeeService;
import com.cattia.service.UserAccountService;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RefugeeOverviewController extends BaseController{

    private final RefugeeService refugeeService;
    private final UserAccountService userAccountService;

    @GetMapping(value = "/refugeeOverview")
    public ModelAndView refugeeOverview(@ModelAttribute(ALERT) AlertDTO alert, final Model model, Authentication authentication) {
        ModelAndView mav = new ModelAndView();
        addModelAttributes(model, authentication);
        List<RefugeeOverviewDto> refugeeList = getRefugeeList(authentication.getName());
        model.addAttribute("refugeeList", refugeeList);
        model.addAttribute(ALERT, alert);
        mav.setViewName("refugeeOverview");
        return mav;
    }

    private List<RefugeeOverviewDto> getRefugeeList(String username) {
        if (userAccountService.isAdmin(username)){
            return refugeeService.getAllRefugees();
        }else{
            return  refugeeService.getAllActiveRefugees();
        }
    }

    @PostMapping(value = "/deactivateRefugee")
    public ModelAndView deactivateRefugee(@RequestParam("refugeeId") Long refugeeId, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        AlertDTO alert;
        int result = refugeeService.deactivateRefugee(refugeeId);
        if (result != 0) {
            alert = new AlertDTO(1, ALERT_SUCCESS, "The refugee was deactivated.");
        } else {
            alert = new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to deactivate the refugee.");
        }
        mav.setViewName("redirect:/refugeeOverview");
        redirectAttributes.addFlashAttribute(ALERT, alert);
        return mav;
    }

    @PostMapping(value = "/activateRefugee")
    public ModelAndView activateRefugee(@RequestParam("refugeeId") Long refugeeId, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        AlertDTO alert;
        int result = refugeeService.activateRefugee(refugeeId);
        if (result != 0) {
            alert = new AlertDTO(1, ALERT_SUCCESS, "The refugee was activated.");
        } else {
            alert = new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to activate the refugee.");
        }
        mav.setViewName("redirect:/refugeeOverview");
        redirectAttributes.addFlashAttribute(ALERT, alert);
        return mav;
    }
}

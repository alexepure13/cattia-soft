package com.cattia.controller;

import com.cattia.dto.AlertDTO;
import com.cattia.dto.UserAccountFormDto;
import com.cattia.dto.UserAccountOverviewDto;
import com.cattia.model.UserRole;
import com.cattia.service.RoleService;
import com.cattia.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserAccountOverviewController extends BaseController {

    private final UserAccountService userAccountService;
    private final RoleService roleService;

    @RequestMapping("/userAccountOverview")
    public String userAccountOverview(@ModelAttribute(ALERT) AlertDTO alert, Model model, Authentication authentication){
        List<UserAccountOverviewDto> userAccountList = userAccountService.getAllUserAccounts();
        addModelAttributes(model, authentication);
        model.addAttribute("userAccountList", userAccountList);
        model.addAttribute(ALERT, alert);
        return "userAccountOverview";
    }

    @PostMapping("/editUserAccount")
    public ModelAndView editUserAccount(@RequestParam("userAccountId") Long userAccountId, final Model model) {
        ModelAndView mav = new ModelAndView();
        UserAccountFormDto userAccount = userAccountService.getUserAccountById(userAccountId);
        if (userAccount == null) {
            model.addAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "User account could not be found, ID: " + userAccountId));
            mav.setViewName("userAccountOverview");
        } else {
            List<UserRole> userRoleList = roleService.listAllRoles();
            mav.addObject("userRoleList", userRoleList);
            mav.setViewName("userAccountEditForm");
            model.addAttribute("userAccount", userAccount);
            model.addAttribute("formAction", FORM_UPDATE);
            mav.setViewName("userAccountEditForm");
        }
        return mav;
    }

    @PostMapping("/editPassword")
    public ModelAndView showEditPasswordForm(@RequestParam("userAccountId") Long userAccountId, Model model) {
        ModelAndView mav = new ModelAndView();
        UserAccountFormDto userAccount = userAccountService.getUserAccountById(userAccountId);
        model.addAttribute("userAccount", userAccount);
        mav.setViewName("userAccountPasswordForm");
        return mav;
    }

    @PostMapping(value = "/deactivateUserAccount")
    public ModelAndView deactivateUserAccount(@RequestParam("userAccountId") Long userAccountId, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        AlertDTO alert;
        int result = userAccountService.deactivateUserAccount(userAccountId);
        if (result != 0) {
            alert = new AlertDTO(1, ALERT_SUCCESS, "The user account was deactivated.");
        } else {
            alert = new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to deactivate the user account.");
        }
        mav.setViewName("redirect:/userAccountOverview");
        redirectAttributes.addFlashAttribute(ALERT, alert);
        return mav;
    }

    @PostMapping(value = "/activateUserAccount")
    public ModelAndView activateUserAccount(@RequestParam("userAccountId") Long userAccountId, final RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        AlertDTO alert;
        int result = userAccountService.activateUserAccount(userAccountId);
        if (result != 0) {
            alert = new AlertDTO(1, ALERT_SUCCESS, "The user account was activated.");
        } else {
            alert = new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to activate the user account.");
        }
        mav.setViewName("redirect:/userAccountOverview");
        redirectAttributes.addFlashAttribute(ALERT, alert);
        return mav;
    }


    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

}

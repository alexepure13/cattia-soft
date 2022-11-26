package com.cattia.controller;

import com.cattia.dto.AlertDTO;
import com.cattia.dto.UserAccountEditFormDto;
import com.cattia.dto.UserAccountFormDto;
import com.cattia.model.UserAccount;
import com.cattia.model.UserRole;
import com.cattia.service.RoleService;
import com.cattia.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserAccountFormController extends BaseController implements ExceptionHandler {

    private final UserAccountService userAccountService;

    private final RoleService roleService;

    @GetMapping("/userAccountForm")
    public ModelAndView userAccountForm(final Model model) {
        ModelAndView mav = new ModelAndView();
        UserAccountFormDto userAccount = new UserAccountFormDto();
        goToUserAccountForm(FORM_CREATE, userAccount, model);
        mav.setViewName("userAccountForm");
        return mav;
    }

    @PostMapping(value = "/submitNewUserAccount", params = "action=saveUserAccountFormAndGoToOverview")
    public ModelAndView saveUserAccountFormAndGoToOverview(@ModelAttribute("formAction") String formAction, @ModelAttribute("userAccount") @Valid UserAccountFormDto userAccount, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        checkIfUserAccountIsUnique(userAccount, bindingResult);
        checkIfEmailIsUnique(userAccount, bindingResult);
        checkPassword(userAccount, bindingResult);
        if (bindingResult.hasErrors()) {
            goToUserAccountForm(formAction, userAccount, model);
            mav.setViewName("userAccountForm");
        } else {
            if (FORM_CREATE.equals(formAction)) {
                var savedUserAccount = userAccountService.saveNewUserAccount(userAccount);
                if (savedUserAccount != null) {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_SUCCESS, "User account '" + userAccount.getUsername() + "' " + " was added successfully"));
                } else {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to save the user account"));
                }
            } else {
                redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "Unknown or wrong form action"));
            }
            mav.setViewName("redirect:/userAccountOverview");
        }
        return mav;
    }

    @PostMapping(value = "/submitExistingUserAccount", params = "action=saveUserAccountFormAndGoToOverview")
    public ModelAndView submitExistingUserAccount(@ModelAttribute("formAction") String formAction, @ModelAttribute("userAccount") @Valid UserAccountEditFormDto userAccount, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        checkIfUserAccountIsUnique(userAccount, bindingResult);
        checkIfEmailIsUnique(userAccount, bindingResult);
        if (bindingResult.hasErrors()) {
            goToUserAccountForm(formAction, userAccount, model);
            mav.setViewName("userAccountEditForm");
        } else {
            if (FORM_UPDATE.equals(formAction)) {
                var savedUserAccount = userAccountService.updateUserAccount(userAccount);
                if (savedUserAccount == 1) {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_SUCCESS, "User account '" + userAccount.getUsername() + "' " + " was updated successfully"));
                } else {
                    redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to update the user account"));
                }
            } else {
                redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "Unknown or wrong form action"));
            }
            mav.setViewName("redirect:/userAccountOverview");
        }
        return mav;
    }


    private void checkPassword(UserAccountFormDto userAccount, BindingResult bindingResult) {
        if (userAccount.getPassword() != null && userAccount.getConfirmPassword() != null && !userAccount.getPassword().equals(userAccount.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userAccount", "password", userAccount.getUsername(), false, null, null, "The password must be identical"));
        }

        if (userAccount.getPassword() != null){
            String pass = userAccount.getPassword();
            boolean isAtLeastOneUpperCase = false;
            boolean isAtLeastOneLowerCase = false;
            boolean isAtLeastOneDigit = false;
            for(int i=0;i<pass.length();i++){
                if(Character.isUpperCase(pass.charAt(i))){
                    isAtLeastOneUpperCase =  true;
                }
                if(Character.isUpperCase(pass.charAt(i))){
                    isAtLeastOneLowerCase =  true;
                }
                if(Character.isDigit(pass.charAt(i))){
                    isAtLeastOneDigit =  true;
                }
            }

            if (!isAtLeastOneUpperCase || !isAtLeastOneLowerCase || !isAtLeastOneDigit){
                bindingResult.addError(new FieldError("userAccount", "password", userAccount.getUsername(), false, null, null, "Password must contain at least one upper case letter, lower case letter and a digit"));
            }

        }


    }

    private void checkIfUserAccountIsUnique(UserAccountEditFormDto userAccount, BindingResult bindingResult) {
        UserAccount foundUserAccount = userAccountService.findByUserAccount(userAccount.getUsername());
        if (foundUserAccount != null && foundUserAccount.getId() != userAccount.getId() && foundUserAccount.getUsername().equals(userAccount.getUsername())) {
            bindingResult.addError(new FieldError("userAccount", "username", userAccount.getUsername(), false, null, null, "The username must be unique (including for deactivated user accounts)"));
        }
    }

    private void checkIfEmailIsUnique(UserAccountEditFormDto userAccount, BindingResult bindingResult){
        UserAccount foundUserAccount = userAccountService.findByEmail(userAccount.getEmail());
        if (foundUserAccount != null && foundUserAccount.getId() != userAccount.getId() && foundUserAccount.getEmail().equals(userAccount.getEmail())) {
            bindingResult.addError(new FieldError("userAccount", "email", userAccount.getEmail(), false, null, null, "The email address must be unique (including for deactivated user accounts)"));
        }
    }

    @PostMapping(value = "/submitNewUserAccount", params = "action=exitWithoutSavingUserAccountForm")
    public ModelAndView exitWithoutSavingUserAccountForm() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/userAccountOverview");
        return mav;
    }

    @PostMapping(value = "/saveNewPassword", params = "action=saveUserAccountPasswordAndGoToOverview")
    public ModelAndView saveUserAccountPasswordAndGoToOverview(@ModelAttribute("userAccount") @Valid UserAccountFormDto userAccount, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        ModelAndView mav = new ModelAndView();
        checkPassword(userAccount, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            mav.setViewName("userAccountPasswordForm");
            return mav;
        } else {
            var savedPassword = userAccountService.updateUserAccountPassword(userAccount);
            if (savedPassword == 1) {
                redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_SUCCESS, "Password for '" + userAccount.getUsername() + "' " + " was changed successfully"));
            } else {
                redirectAttributes.addFlashAttribute(ALERT, new AlertDTO(1, ALERT_ERROR, "An error occurred while trying to change the password"));
            }
            mav.setViewName("redirect:/userAccountOverview");
            return mav;
        }
    }

    @PostMapping(value = "/saveNewPassword", params = "action=exitWithoutSavingPassword")
    public ModelAndView saveNewPassword() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/userAccountOverview");
        return mav;
    }

    @Override
    public Class<? extends Throwable>[] value() {
        return new Class[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    private void goToUserAccountForm(String formAction, UserAccountEditFormDto userAccount, Model model) {
        List<UserRole> userRoleList = roleService.listAllRoles();
        model.addAttribute("userAccount", userAccount);
        model.addAttribute("userRoleList", userRoleList);
        model.addAttribute("formAction", formAction);
    }


}
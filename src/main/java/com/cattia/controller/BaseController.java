package com.cattia.controller;

import com.cattia.security.UserAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;

import java.util.stream.Collectors;

public class BaseController {

    public static final String FORM_CREATE = "CREATE";
    public static final String FORM_UPDATE = "UPDATE";
    public static final String FORM_ADD_STOCK = "addToStock";
    public static final String FORM_REMOVE_STOCK = "removeFromStock";

    protected static final String ALERT = "alert";
    protected static final int ACTIVE = 1;
    protected static final String ALERT_SUCCESS = "success";
    protected static final String ALERT_ERROR = "error";
    protected static final String ALERT_INFO = "info";

    @Autowired
    public UserAccountDetailsService userAccountDetailsService;


    public void addModelAttributes(Model model, Authentication authentication) {
        model.addAttribute("loggedUser", userAccountDetailsService.getUserDataById(authentication.getName()));
        model.addAttribute("userPermissions", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }
}

package com.cattia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LicenceController {

    @GetMapping(value="/licence")
    public ModelAndView index () {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("licence");
        return mav;
    }
}

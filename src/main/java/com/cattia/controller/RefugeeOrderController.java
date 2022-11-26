package com.cattia.controller;

import com.cattia.dto.RefugeeOrderConfirmationDto;
import com.cattia.dto.RefugeeOrderDto;
import com.cattia.dto.StockErrorReportDto;
import com.cattia.service.ProductService;
import com.cattia.service.RefugeeOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Controller

public class RefugeeOrderController extends BaseController {

    public static final String REDIRECT_TO_ASSIGN_PRODUCTS_PAGE = "redirect:/assignProducts";
    private final RefugeeOrderService refugeeOrderService;
    private final ProductService productService;

    @PostMapping(value = "/assignProducts")
    public ModelAndView assignProducts(@ModelAttribute("refugeeId") Long refugeeId, final Model model) {
        ModelAndView mav = new ModelAndView();
        RefugeeOrderDto refugeeOrder = refugeeOrderService.processOrder(refugeeId);
        model.addAttribute("refugeeOrder", refugeeOrder);
        model.addAttribute("productList", productService.getAllActiveAndAvailableProducts(refugeeOrder));
        mav.setViewName("order");
        return mav;
    }

    @PostMapping(value = "/saveToOrder")
    public ModelAndView saveToOrder(@ModelAttribute("orderId") Long orderId, @ModelAttribute("refugeeId") Long refugeeId, @ModelAttribute("productId") Long productId, RedirectAttributes redirectAttributes, HttpServletRequest request, Authentication authentication) {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        ModelAndView mav = new ModelAndView();
        Long savedRefugeeId = refugeeOrderService.saveOrderItem(orderId, refugeeId, productId, authentication.getName());
        redirectAttributes.addFlashAttribute("refugeeId", refugeeId);
        mav.setViewName(REDIRECT_TO_ASSIGN_PRODUCTS_PAGE);
        return mav;
    }

    @PostMapping(value = "/saveOrderItem")
    public ModelAndView saveOrderItem(@ModelAttribute("orderId") Long orderId, @ModelAttribute("orderItemId") Long orderItemId, @ModelAttribute("openOrderItemQuantity") String quantity, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        ModelAndView mav = new ModelAndView();
        Long refugeeId;
        try {
            refugeeId = saveOrderItem(orderItemId, quantity);
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("orderErrorMessage", e.getMessage());
            refugeeId = refugeeOrderService.getOrderById(orderId);
        }
        redirectAttributes.addFlashAttribute("refugeeId", refugeeId);
        mav.setViewName(REDIRECT_TO_ASSIGN_PRODUCTS_PAGE);
        return mav;
    }

    private Long saveOrderItem(Long orderItemId, String quantity) {
        if (quantity == null || quantity.length() == 0) {
            throw new NumberFormatException("Cannot be empty");
        }

        BigDecimal newQuantity = new BigDecimal(quantity);
        if (newQuantity.compareTo(BigDecimal.ONE) < 0) {
            throw new NumberFormatException("Must be a positive number");
        }

        if (newQuantity.compareTo(new BigDecimal(1000)) > 0) {
            throw new NumberFormatException("Must be lower than 9999");
        }

        Long refugeeId = refugeeOrderService.updateOrderItemQuantity(orderItemId, newQuantity);
        return refugeeId;
    }


    @PostMapping(value = "/removeOrderItem")
    public ModelAndView removeOrderItem(@ModelAttribute("orderItemId") Long orderItemId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        ModelAndView mav = new ModelAndView();
        Long refugeeId = refugeeOrderService.removeOrderItem(orderItemId);
        redirectAttributes.addFlashAttribute("refugeeId", refugeeId);
        mav.setViewName(REDIRECT_TO_ASSIGN_PRODUCTS_PAGE);
        return mav;
    }

    @PostMapping(value = "/confirmOrder")
    public ModelAndView confirmOrder(@ModelAttribute("id") Long refugeeOrderId, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        StockErrorReportDto stockErrorReport = refugeeOrderService.validateStockQuantity(refugeeOrderId);
        if (stockErrorReport.getStockErrorMessageList().isEmpty()) {
            RefugeeOrderConfirmationDto refugeeOrderConfirmation = refugeeOrderService.getRefugeeOrderConfirmationById(refugeeOrderId);
            model.addAttribute("refugeeOrderConfirmation", refugeeOrderConfirmation);
            mav.setViewName("/orderConfirmation");
        } else {
            request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
            redirectAttributes.addFlashAttribute("stockValidationErrors", stockErrorReport.getStockErrorMessageList());
            redirectAttributes.addFlashAttribute("refugeeId", stockErrorReport.getRefugeeId());
            mav.setViewName(REDIRECT_TO_ASSIGN_PRODUCTS_PAGE);
        }
        return mav;
    }

}

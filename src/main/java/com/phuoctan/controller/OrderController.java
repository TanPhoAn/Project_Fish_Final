package com.phuoctan.controller;


import com.phuoctan.entity.*;
import com.phuoctan.service.CartService;
import com.phuoctan.service.OrderService;
import jakarta.persistence.criteria.Order;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;



    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/payment")
    public String payment(@AuthenticationPrincipal CustomerUserDetails customer, Model model) {
        Customer user =  customer.getCustomer();
        List<Cart_item> orderItem = cartService.getCartItems(user);
        Long totalPrice = orderItem.stream().mapToLong(Cart_item::getTotalPrice).sum();

        //session.setAttribute("cartItemList", orderItem);
        model.addAttribute("user", user);
        model.addAttribute("orderItem", orderItem);
        model.addAttribute("totalPrice", totalPrice);


        return "page/order";
    }

    @PostMapping("order/processing")
    public String loading( @RequestParam String paymentMethod, HttpSession session, RedirectAttributes ra, @AuthenticationPrincipal CustomerUserDetails customer) {
        List<Cart_item> orderItem = cartService.getCartItems(customer.getCustomer());

        if(orderItem == null){
            ra.addFlashAttribute("error", "Cart is empty!");
            return "redirect:/payment";
        }
        else if(paymentMethod == null || paymentMethod.isBlank()){
            ra.addFlashAttribute("error", "Please choose a payment method");
            return "redirect:/payment";
        }
        session.setAttribute("paymentMethod", paymentMethod);
        return "redirect:/common/loading";
    }

    @GetMapping("/common/loading")
    public String loading(HttpSession session) {
        if(session.getAttribute("paymentMethod") == null){
            return "redirect:/payment";
        }
        return "/common/loading";
    }

    @GetMapping("/order/confirmation")
    public String confirmation(@AuthenticationPrincipal CustomerUserDetails customer, HttpSession session, Model model) {
        PaymentMethod method = PaymentMethod.fromString(session.getAttribute("paymentMethod"));
        if(method == null ){
            return "redirect:/payment";
        }
        else{

            Orders orders = orderService.makeOrder(customer.getCustomer(), method);
            List<Order_item> orderItemList = orderService.getOrderItems(orders.getId());




            model.addAttribute("orderItemList", orderItemList);
            model.addAttribute("order", orders);
            model.addAttribute("paymentMethod", method);
            session.removeAttribute("paymentMethod");
        }
        return  "page/order-confirmation";
    }

    @GetMapping("/order/details/{orderId}")
    public String details(@PathVariable("orderId") Integer orderId, Model model ) {
        Orders order = orderService.getOrder(orderId).orElseThrow(() -> new RuntimeException("order not found"));
        List<Order_item> orderItemList   = orderService.getOrderItems(order.getId());

        model.addAttribute("order", order);
        model.addAttribute("orderItemList", orderItemList);
        return "page/order-detail";

    }

    @PostMapping("/order/details/{orderId}/remove")
    public String removeOrder(@PathVariable("orderId") Integer orderId, HttpSession session) {
        session.setAttribute("orderId", orderId);
        return  "redirect:/order/remove/processing";
    }

    @GetMapping("/order/remove/processing")
    public String loading2() {

            return "/common/loading-remove-order";

    }
    @GetMapping("/order/remove-confirmation")
    public String removeConfirmation(HttpSession session) {
        Integer  orderId = Integer.parseInt(session.getAttribute("orderId").toString());
        Orders orderDeleted = orderService.getOrder(orderId).orElseThrow(() -> new RuntimeException("order not found"));
        orderService.deleteOrder(orderDeleted);
        session.removeAttribute("orderId");
        return "/page/remove-order-confirm";
    }

}

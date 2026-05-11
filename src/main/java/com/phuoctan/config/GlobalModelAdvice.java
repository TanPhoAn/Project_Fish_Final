package com.phuoctan.config;


import com.phuoctan.entity.Customer;
import com.phuoctan.entity.CustomerUserDetails;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("currentUser")
    public Customer currentUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof CustomerUserDetails userDetails){
            return  userDetails.getCustomer(); //take entity io auth
        }
        return null;
    }
}

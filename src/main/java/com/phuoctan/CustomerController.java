package com.phuoctan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String home()
    {
        return "/page/index";
    }

    @GetMapping("/register")
    public String register(Model model)
    {
        registerForm registerForm = new registerForm();
        model.addAttribute("registerForm", registerForm);
        return "/page/sign-up";
    }

    @PostMapping("/register/save")
    public String saveForm(Model model, @ModelAttribute("registerForm")  registerForm registerForm)
    {
        model.addAttribute("registerForm", registerForm);
        if(registerForm.getUsername() == null || registerForm.getPassword() == null || registerForm.getConfirmPassword() == null){

        }
        Customer customer = new Customer();
        customer.setName(registerForm.getName());
        customer.setAddress(registerForm.getAddress());
        customer.setPhone(registerForm.getPhone());
        customer.setEmail(registerForm.getEmail());
        customer.setPassword(registerForm.getPassword());
        customer.setUsername(registerForm.getUsername());

        customerService.insertCustomer(customer);

        return "/page/index";
    }
}

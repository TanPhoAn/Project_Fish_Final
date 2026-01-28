package com.phuoctan;

import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String home() {
        return "/page/index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        registerFormDTO registerFormDTO = new registerFormDTO();
        model.addAttribute("registerForm", registerFormDTO);
        return "/page/sign-up";
    }

    @PostMapping("/register/save")
    public String saveForm(Model model, @ModelAttribute("registerForm") registerFormDTO registerFormDTO) {
        model.addAttribute("registerForm", registerFormDTO);
        if (registerFormDTO.getPassword() == null || registerFormDTO.getConfirmPassword() == null) {
            System.out.println("form is null");
        }
//        no need to use this one anymore thanks to mapstruct :D
//        Customer customer = new Customer();
//
//        customer.setName(registerFormDTO.getName());
//        customer.setAddress(registerFormDTO.getAddress());
//        customer.setPhone(registerFormDTO.getPhone());
//        customer.setEmail(registerFormDTO.getEmail());
//        customer.setPassword(registerFormDTO.getPassword());
//        customer.setUsername(registerFormDTO.getUsername());
        Customer customer = customerMapper.toEntity(registerFormDTO);
        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        customer.setRole("user");
        customerService.insertCustomer(customer);

        return "/page/index";
    }

    @GetMapping("/login")
    public String login() {

        return "/page/sign-in";
    }
}
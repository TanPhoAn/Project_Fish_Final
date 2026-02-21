package com.phuoctan.controller;

import com.phuoctan.entity.Customer;
import com.phuoctan.CustomerMapper;
import com.phuoctan.entity.Product;
import com.phuoctan.entity.ProductCategory;
import com.phuoctan.service.CustomerService;
import com.phuoctan.dto.registerFormDTO;
import com.phuoctan.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);
    private final ProductService productService;

    public CustomerController(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        //take fish list
        Map<ProductCategory, List<Product>> productsByCategory = new LinkedHashMap<>();
        for(ProductCategory category :  ProductCategory.values() ){
            productsByCategory.put(
                    category,
                    productService.findByCategory(category)
            );
        }
        model.addAttribute("productsByCategory", productsByCategory);
        //take user info

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

        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {

        return "/page/sign-in";
    }
}
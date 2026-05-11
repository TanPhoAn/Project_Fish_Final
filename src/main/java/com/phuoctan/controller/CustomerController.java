package com.phuoctan.controller;

import com.phuoctan.entity.*;
import com.phuoctan.CustomerMapper;
import com.phuoctan.service.CustomerService;
import com.phuoctan.dto.registerFormDTO;
import com.phuoctan.service.OrderService;
import com.phuoctan.service.ProductService;

import org.mapstruct.factory.Mappers;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



@Controller
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);
    private final ProductService productService;
    private final  OrderService orderService;

    public CustomerController(CustomerService customerService, ProductService productService, OrderService orderService) {
        this.customerService = customerService;
        this.productService = productService;

        this.orderService = orderService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        //take fish list
        Map<ProductCategory, List<Product>> productsByCategory = new LinkedHashMap<>();
        for(ProductCategory category :  ProductCategory.values() ){
            productsByCategory.put(
                    category,
                    productService.findByCategory(category)
            );
        }
        model.addAttribute("productsByCategory", productsByCategory);


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

    @GetMapping("/user/profile")
    public String users(Model model, @AuthenticationPrincipal CustomerUserDetails  customer) {

        Customer customerDetail = customerService.getCustomer(customer.getCustomer().getId());
        model.addAttribute("customer", customerDetail);
        List<Orders> orderList = orderService.getOrders(customerDetail);

        model.addAttribute("orderList", orderList);
        return "/page/user-detail";
    }

    @GetMapping("/user/profile/edit")
    public String edit(Model model, @AuthenticationPrincipal CustomerUserDetails  customer) {
        Customer customerDetail = customer.getCustomer();
        model.addAttribute("customerDetail", customerDetail);
        return "/common/user-edit :: user-profile";
    }

    @PostMapping("/user/profile/edit/saving")
    public String saveUserInfo(@ModelAttribute("customerDetail") Customer newInfoCustomer, Model model, @AuthenticationPrincipal CustomerUserDetails  customer) {

        model.addAttribute("customerDetail", newInfoCustomer);
        customerService.updateCustomer(newInfoCustomer);
        customer.setCustomer(newInfoCustomer);
        return "redirect:/user/profile";
    }


    @PostMapping("/user/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal CustomerUserDetails  customer) {
        if(file.isEmpty()){
            return "redirect:/page/user-detail";
        }
        try{
            Customer user = customer.getCustomer();
            //naming file to new one
            String originalFilename = file.getOriginalFilename();
            String extension =  originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = user.getId() + "_" + System.currentTimeMillis() + extension;

            //patch to save
            String uploadDir = "/Users/phanphuoctan/IdeaProjects/Project_Fish_Final/uploads/user-avatar/";
            Path path = Paths.get(uploadDir + newFileName);

            //save avatar file
            Files.write(path, file.getBytes());
            // delete old ava
            if (user.getAvatar() != null && !user.getAvatar().equals("default.png")) {
                Path oldPath = Paths.get(uploadDir + user.getAvatar());
                Files.deleteIfExists(oldPath);
            }
            //update DB
            customerService.setAvatar(user, newFileName);

            customer.getCustomer().setAvatar(newFileName);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/user/profile";

    }
    @GetMapping("/cv")
    public String cv() {
        return "page/cv";
    }
//    @PostMapping("/user/upload-avatar")
//    @ResponseBody
//    public String uploadAvatarr(@RequestParam("avatar") MultipartFile file,
//                               @AuthenticationPrincipal CustomerUserDetails customer) {
//
//        if (file.isEmpty()) {
//            return "error";
//        }
//
//        try {
//            Customer user = customer.getCustomer();
//
//            String originalFilename = file.getOriginalFilename();
//            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            String newFileName = user.getId() + "_" + System.currentTimeMillis() + extension;
//
//            String uploadDir = "src/main/resources/static/images/user-avatar/";
//            Path path = Paths.get(uploadDir + newFileName);
//
//            Files.write(path, file.getBytes());
//
//            // update DB
//            customerService.setAvatar(user, newFileName);
//
//            // update session
//            customer.getCustomer().setAvatar(newFileName);
//
//            return newFileName; // 👈 trả filename về cho JS
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }
}
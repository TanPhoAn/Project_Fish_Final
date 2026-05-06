package com.phuoctan.controller;

import com.phuoctan.entity.*;
import com.phuoctan.service.AdminService;
import com.phuoctan.service.CustomerService;
import com.phuoctan.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final CustomerService customerService;
    private final ProductService productService;

    public AdminController(AdminService adminService, CustomerService customerService, ProductService productService) {
        this.adminService = adminService;
        this.customerService = customerService;

        this.productService = productService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model){
        Integer totalCus = adminService.totalCustomer().size();
        Integer totalOrder =  adminService.totalOrders().size();
        Integer pendingOrders = adminService.totalOrdersWithStatus(OrderStatus.PENDING).size();
        Integer totalProducts = adminService.totalProducts().size();
        List<Customer> customerList = adminService.totalCustomer();
        List<Orders> ordersList = adminService.totalOrders();
        List<Product> productList = adminService.totalProducts();

        model.addAttribute("categoryList", ProductCategory.values());
        model.addAttribute("totalCus",totalCus);
        model.addAttribute("totalOrder",totalOrder);
        model.addAttribute("pendingOrders",pendingOrders);
        model.addAttribute("totalProducts",totalProducts);
        model.addAttribute("userList",customerList);
        model.addAttribute("ordersList",ordersList);
        model.addAttribute("productList",productList);
        return "page/adminDashboard";
    }

    @PostMapping("/admin/user/{userId}/update")
    public String updateUser(@PathVariable Integer userId,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String phone,
                             @RequestParam String address){
        Customer currentCustomer = customerService.getCustomer(userId);
        currentCustomer.setName(name);
        currentCustomer.setAddress(address);
        currentCustomer.setEmail(email);
        currentCustomer.setPhone(phone);


        customerService.updateNewCustomer(currentCustomer);

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/user/{userId}/delete")
    public String deleteUser(@PathVariable Integer userId){

        customerService.deleteCustomer(customerService.getCustomer(userId));

    return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(
            @RequestParam String productName,
            @RequestParam ProductCategory productCategory,
            @RequestParam(defaultValue = "0") Double productPrice,
            @RequestParam(defaultValue = "0") int quantity,
            @RequestParam(required = false) String productDescription,
            @RequestParam(required = false) MultipartFile imageFile
    ) {
        try {
            String savedFileName = null;

            if (imageFile != null && !imageFile.isEmpty()) {
                savedFileName = saveProductImage(imageFile, productCategory);
            }

            Product product = new Product(
                    productName,
                    productDescription,
                    productPrice,
                    productCategory,
                    savedFileName,
                    quantity
            );

            productService.createProduct(product);
            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard";
        }
    }


    private String saveProductImage(MultipartFile imageFile, ProductCategory category) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFileName = System.currentTimeMillis() + extension;

        Path uploadDir = Paths.get(
                System.getProperty("user.dir"),
                "uploads",
                category.name()
        );
        Files.createDirectories(uploadDir);

        Path filePath = uploadDir.resolve(newFileName);
        imageFile.transferTo(filePath.toFile());

        return newFileName;
    }
    @PostMapping("/admin/product/{id}/remove")
    public String deleteProductById(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return "redirect:/admin/dashboard";
    }
}

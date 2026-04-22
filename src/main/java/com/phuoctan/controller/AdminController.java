package com.phuoctan.controller;

import com.phuoctan.entity.Customer;
import com.phuoctan.entity.OrderStatus;
import com.phuoctan.entity.Orders;
import com.phuoctan.entity.Product;
import com.phuoctan.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class AdminController {
    private final AdminService adminService;


    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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

        model.addAttribute("totalCus",totalCus);
        model.addAttribute("totalOrder",totalOrder);
        model.addAttribute("pendingOrders",pendingOrders);
        model.addAttribute("totalProducts",totalProducts);
        model.addAttribute("userList",customerList);
        model.addAttribute("ordersList",ordersList);
        model.addAttribute("productList",productList);
        return "page/adminDashboard";
    }



}

package com.phuoctan.controller;

import com.phuoctan.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model){
        Integer totalCus = adminService.totalCustomer();

        model.addAttribute("totalCus",totalCus);
        return "page/adminDashboard";
    }
}

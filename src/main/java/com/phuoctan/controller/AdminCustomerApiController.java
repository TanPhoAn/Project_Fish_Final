package com.phuoctan.controller;

import com.phuoctan.dto.CustomerDTO;
import com.phuoctan.entity.Customer;
import com.phuoctan.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/")

public class AdminCustomerApiController {
    private final CustomerService customerService;

    public AdminCustomerApiController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/users/{id}")
    public CustomerDTO getCustomers(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }
    // @PostMapping("/users/{id}/save-changes")
    // public String saveCustomers(@PathVariable Integer id, @RequestBody
    // CustomerDTO customerDTO) {
    //
    // }

}

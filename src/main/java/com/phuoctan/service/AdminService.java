package com.phuoctan.service;


import com.phuoctan.entity.Customer;
import com.phuoctan.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final CustomerRepository customerRepository;

    public AdminService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    public Integer totalCustomer(){
        return customerRepository.findAll().size();
    }
}

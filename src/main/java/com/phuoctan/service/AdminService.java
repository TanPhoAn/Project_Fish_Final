package com.phuoctan.service;


import com.phuoctan.entity.Customer;
import com.phuoctan.entity.OrderStatus;
import com.phuoctan.entity.Orders;
import com.phuoctan.entity.Product;
import com.phuoctan.repository.CustomerRepository;
import com.phuoctan.repository.OrderRepository;
import com.phuoctan.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public AdminService(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Customer> totalCustomer(){
        return customerRepository.findAll();
    }

    public List<Orders> totalOrders(){
        return orderRepository.findAll();
    }

    public List<Orders> totalOrdersWithStatus(OrderStatus orderStatus){
        return orderRepository.findAllByStatus(orderStatus);
    }

    public List<Product> totalProducts(){
        return productRepository.findAll();
    }




}

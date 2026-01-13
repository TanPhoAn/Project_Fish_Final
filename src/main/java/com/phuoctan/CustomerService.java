package com.phuoctan;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
//this class available as a bean -> spring instantiate(khoi tao) -> use in other classes
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> getcustomersList() {
        return customerRepository.findAll();
    }

    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}

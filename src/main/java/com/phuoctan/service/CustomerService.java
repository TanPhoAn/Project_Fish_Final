package com.phuoctan.service;

import com.phuoctan.entity.Customer;
import com.phuoctan.CustomerMapper;
import com.phuoctan.dto.CustomerDTO;
import com.phuoctan.repository.CustomerRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//this class available as a bean -> spring instantiate(khoi tao) -> use in other classes
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public CustomerDTO getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Customer not found !") );
        return customerMapper.customerTocustomerDTO(customer);
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



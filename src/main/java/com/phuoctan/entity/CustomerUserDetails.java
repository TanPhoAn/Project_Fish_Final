package com.phuoctan.entity;

import com.phuoctan.entity.Customer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class CustomerUserDetails extends User {

    private final Customer customer;

    public CustomerUserDetails(Customer customer) {
        super(
                customer.getEmail(),
                customer.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + customer.getRole()))
        );
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}

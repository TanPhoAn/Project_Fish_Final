package com.phuoctan.service;

import com.phuoctan.CustomerMapper;
import com.phuoctan.entity.Customer;
import com.phuoctan.entity.CustomerUserDetails;
import com.phuoctan.repository.CustomerRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;


@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    public CustomerUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Customer customer = customerRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

//        return User.builder()
//                .username(customer.getEmail())
//                .password(customer.getPassword())
//                .roles(customer.getRole())
//                .disabled(!customer.isStatus())
//                .build();
        return new CustomerUserDetails(customer);
    }
}

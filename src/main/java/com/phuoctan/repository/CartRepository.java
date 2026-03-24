package com.phuoctan.repository;

import com.phuoctan.entity.Cart;
import com.phuoctan.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByCustomer(Customer customer);
}

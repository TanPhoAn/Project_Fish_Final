package com.phuoctan.repository;

import com.phuoctan.entity.Cart;
import com.phuoctan.entity.Cart_item;
import com.phuoctan.entity.Customer;
import com.phuoctan.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository  extends JpaRepository<Cart_item, Integer> {
    Optional<Cart_item> findByCartAndProduct(Cart cart, Product product);

    List<Cart_item> findByCart(Cart  cart);
}

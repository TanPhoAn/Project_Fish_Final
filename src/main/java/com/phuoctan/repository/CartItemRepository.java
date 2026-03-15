package com.phuoctan.repository;

import com.phuoctan.entity.Cart_item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository<Cart_item, Integer> {
}

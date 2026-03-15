package com.phuoctan.repository;

import com.phuoctan.entity.Order_item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<Order_item, Integer> {
}

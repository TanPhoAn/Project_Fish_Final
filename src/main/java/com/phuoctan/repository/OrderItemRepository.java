package com.phuoctan.repository;

import com.phuoctan.entity.Order_item;
import com.phuoctan.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<Order_item, Integer> {
    Optional<Order_item> findByOrders(Orders orders);
    List<Order_item> findByOrders_Id(Integer orders_id);
}

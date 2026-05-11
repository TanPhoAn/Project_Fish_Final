package com.phuoctan.repository;

import com.phuoctan.entity.Customer;
import com.phuoctan.entity.OrderStatus;
import com.phuoctan.entity.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    Optional<Orders> findByCustomer(Customer customer);
    Optional<Orders> findById(Integer id);
    List<Orders> findAllByCustomer(Customer customer);
    List<Orders> findAllByStatus(OrderStatus orderStatus);


    @Query("""
    SELECT o FROM Orders o
    WHERE (:keyword = '' OR LOWER(o.customer.name) LIKE LOWER(CONCAT('%',:keyword,'%')))
    AND(:status IS NULL OR  o.status = :status)
""")
    List<Orders> searchOrders(String keyword, OrderStatus status);
}

package com.phuoctan.entity;


import jakarta.persistence.*;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order_item> order_items;

    private LocalDateTime order_date;
    private Long total_price;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

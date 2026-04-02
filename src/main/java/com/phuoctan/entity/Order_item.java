package com.phuoctan.entity;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Order_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName = "id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;

    private Long price;
    private Integer quantity;
    private Long totalPrice;


    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order_item orderItem = (Order_item) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(orders, orderItem.orders) && Objects.equals(product, orderItem.product) && Objects.equals(price, orderItem.price) && Objects.equals(quantity, orderItem.quantity) && Objects.equals(totalPrice, orderItem.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orders, product, price, quantity, totalPrice);
    }

    public Orders getOrder() {
        return orders;
    }

    public void setOrder(Orders orders) {
        this.orders = orders;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



}

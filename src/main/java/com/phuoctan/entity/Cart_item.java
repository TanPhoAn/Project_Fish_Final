package com.phuoctan.entity;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Cart_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @ManyToOne
    @JoinColumn(name="cart_id", referencedColumnName = "id")
    private Cart cart;


    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;

    private Integer quantity;
    private Long price;
    private Long totalPrice;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart_item cartItem = (Cart_item) o;
        return Objects.equals(id, cartItem.id) && Objects.equals(cart, cartItem.cart) && Objects.equals(product, cartItem.product) && Objects.equals(quantity, cartItem.quantity) && Objects.equals(price, cartItem.price) && Objects.equals(totalPrice, cartItem.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cart, product, quantity, price, totalPrice);
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}

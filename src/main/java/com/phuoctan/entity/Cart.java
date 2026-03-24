package com.phuoctan.entity;


import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name="customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart_item> cart_items;



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id == cart.id && Objects.equals(customer, cart.customer) && Objects.equals(cart_items, cart.cart_items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, cart_items);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Cart_item> getCart_items() {
        return cart_items;
    }

    public void setCart_items(List<Cart_item> cart_items) {
        this.cart_items = cart_items;
    }




}

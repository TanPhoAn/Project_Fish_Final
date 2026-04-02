package com.phuoctan.service;


import com.phuoctan.entity.Cart;
import com.phuoctan.entity.Cart_item;
import com.phuoctan.entity.Customer;
import com.phuoctan.entity.Product;
import com.phuoctan.repository.CartItemRepository;
import com.phuoctan.repository.CartRepository;
import com.phuoctan.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,  ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;

        this.productRepository = productRepository;
    }

    //add product to cart
    public void addToCart(Customer customer, int productId) {
        Cart cart = cartRepository.findByCustomer(customer).orElseGet(()->{
            Cart cart1 = new Cart();
            cart1.setCustomer(customer);
            return cartRepository.save(cart1);

        });

        Product product = productRepository.findById(productId).orElseThrow(
                ()->new RuntimeException("Product not found !"));

        Optional<Cart_item> cartExist=  cartItemRepository.findByCartAndProduct(cart,product);
        if(cartExist.isPresent()){
            Cart_item cart_item = cartExist.get();
            cart_item.setQuantity(cart_item.getQuantity()+1);
            cart_item.setPrice(cart_item.getProduct().getProductPrice());
            cart_item.setTotalPrice(cart_item.getPrice()*cart_item.getQuantity());
            cartItemRepository.save(cart_item);
        }else{
            Cart_item cart_item = new Cart_item();
            cart_item.setProduct(product);
            cart_item.setCart(cart);
            cart_item.setQuantity(1);
            cart_item.setPrice(cart_item.getProduct().getProductPrice());
            cart_item.setTotalPrice(cart_item.getPrice()*cart_item.getQuantity());
            cartItemRepository.save(cart_item);
        }
    }


    public List<Cart_item> getCartItems(Customer customer) {
        Cart  cart = cartRepository.findByCustomer(customer).orElseThrow(null);
        if(cart == null){
            return new ArrayList<>();
        }
        return cartItemRepository.findByCart(cart);
    }
    public Cart  getCart(Customer customer) {
        return cartRepository.findByCustomer(customer).orElseThrow(null);
    }
    public void deleteByCart(Cart cart){
        cartItemRepository.deleteAll(cartItemRepository.findByCart(cart));
    }


    public void removeCart(int cartId){
        cartRepository.deleteById(cartId);
    }
    public void removeCartItem(int itemId){
        cartItemRepository.deleteById(itemId);
    }
    public void updateQuantity(int itemId, int quantity) {
        Cart_item cart_item = cartItemRepository.findById(itemId).orElseThrow(null);
        cart_item.setQuantity(quantity);
        cartItemRepository.save(cart_item);
    }
}

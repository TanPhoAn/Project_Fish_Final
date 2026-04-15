package com.phuoctan.service;


import com.phuoctan.OrderMapper;
import com.phuoctan.entity.*;
import com.phuoctan.repository.CartItemRepository;
import com.phuoctan.repository.CartRepository;
import com.phuoctan.repository.OrderItemRepository;
import com.phuoctan.repository.OrderRepository;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private  final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository,  CartService cartService, OrderItemRepository orderItemRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;

        this.cartItemRepository = cartItemRepository;
    }

    //create order_item
    public Orders makeOrder(Customer customer, PaymentMethod paymentMethod){
        //create order

            Orders order = new Orders();
            order.setCustomer(customer);
            order.setStatus(OrderStatus.PENDING);
            order.setPaymentMethod(paymentMethod);
            orderRepository.save(order);

        //copy cart_item to order_item
        //get cart item
        List<Cart_item> itemList =  cartService.getCartItems(customer);

        for(Cart_item cart_item:itemList){
                Order_item order_item = orderMapper.cartItemToOrderItem(cart_item);
                order_item.setOrders(order);
                orderItemRepository.save(order_item);

            }

        List<Order_item> orderItemList = this.getOrderItems(order.getId());

        order.setTotal_price(orderItemList.stream().mapToLong(Order_item::getTotalPrice).sum());


        //delete cart
        Cart cart = cartService.getCart(customer);
        cartService.deleteByCart(cart);
        cartRepository.save(cart);
        orderRepository.save(order);
        return  order;
        }



    //get order
    public List<Orders> getOrders(Customer customer) {
        return orderRepository.findAllByCustomer(customer);
    }

    //get order item
    public List<Order_item> getOrderItems(Integer orders_id) {
        return orderItemRepository.findByOrders_Id(orders_id);
    }
    //update order
    public void updateOrder(Orders order){
        orderRepository.save(order);
    }

    public Optional<Orders> getOrder(Integer order_id){
        return orderRepository.findById(order_id);
    }

    public void deleteOrder(Orders order){
        orderRepository.delete(order);
    }
    //remove order
}

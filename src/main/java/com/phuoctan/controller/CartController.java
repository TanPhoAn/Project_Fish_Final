package com.phuoctan.controller;



import com.phuoctan.entity.Cart_item;
import com.phuoctan.entity.CustomerUserDetails;
import com.phuoctan.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam int productId,
                            @AuthenticationPrincipal CustomerUserDetails userDetails) {

        cartService.addToCart(userDetails.getCustomer(), productId);

        return "product added";
    }

    //popup
    @GetMapping("/popup")
    //@ResponseBody
    public String popup(Model model,@AuthenticationPrincipal CustomerUserDetails userDetails) {

        List<Cart_item> itemList = cartService.getCartItems(userDetails.getCustomer());
        model.addAttribute("itemList", itemList);
        Long totalPrice = itemList.stream().mapToLong(Cart_item::getTotalPrice).sum();
        model.addAttribute("totalPrice", totalPrice);
        return "common/cart-popup :: cartContent";
    }

    //remove item
    @PostMapping("/remove")
    @ResponseBody
    public void removeCartItem(@RequestParam int itemId){
        cartService.removeCartItem(itemId);

    }



    // UPDATE QUANTITY
    @PostMapping("/update")
    @ResponseBody
    public String updateQty(@RequestParam int itemId,
                            @RequestParam int quantity){

        cartService.updateQuantity(itemId, quantity);

        return "quantity updated";
    }
}

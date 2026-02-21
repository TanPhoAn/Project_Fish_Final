package com.phuoctan.controller;

import com.phuoctan.entity.Product;
import com.phuoctan.entity.ProductCategory;
import com.phuoctan.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/fish")
    public String fishList(Model model){

        List<Product> fishList = productService.findByCategory(ProductCategory.FISH);
        model.addAttribute("fishList", fishList);
        return "/page/index";
    }



}

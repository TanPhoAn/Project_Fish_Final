package com.phuoctan.controller;

import com.phuoctan.entity.Product;
import com.phuoctan.entity.ProductCategory;
import com.phuoctan.service.CartService;
import com.phuoctan.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Template chung cho danh sách sản phẩm theo category.
     * URL dạng: /products/fish, /products/plant, /products/fish-food, ...
     */
    @GetMapping("/products/{categorySlug}")
    public String listByCategory(
            @PathVariable("categorySlug") String categorySlug,
            Model model,
            @RequestParam(value="page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(defaultValue = "") String sort


    ) {
        // "fish-food" -> "FISH_FOOD"
        String enumName = categorySlug.toUpperCase().replace("-", "_");
        int zeroBasedPage = Math.max(page - 1, 0);
        ProductCategory category;
        try {
            //get category
            category = ProductCategory.valueOf(enumName);
        } catch (IllegalArgumentException ex) {
            return "redirect:/home";
        }
        Page<Product> productPage = productService.findByCategoryPage(category, zeroBasedPage, size, min, max, keyword, sort);
        //List<Product> products = productService.findByCategory(category );

        model.addAttribute("category", category);
        model.addAttribute("categorySlug", categorySlug);
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);

        // input user
        model.addAttribute("keyword", keyword);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("sort", sort);
        return "/page/product-lists";
    }



}

package com.phuoctan.controller;

import com.phuoctan.entity.Product;
import com.phuoctan.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/api/products")
public class AdminProductApiController {
    private final ProductService productService;

    public AdminProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductSearchResponse searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String sort
    ) {
        int size = productService.findAll().size();
        Page<Product> productPage = productService.searchProducts(page, size, keyword,  sort);

        List<ProductSummary> items = productPage.getContent().stream()
                .map(product -> new ProductSummary(
                        product.getId(),
                        product.getProductName(),
                        product.getProductDescription(),
                         product.getProductPrice(),
                        product.getQuantity(),
                        product.getProductStatus(),
                        product.getProductCategory().name(),
                        product.getProductCategory().getLabel(),
                        "/uploads/" + product.getProductCategory().name() + "/" + product.getProductImage()
                ))
                .toList();

        return new ProductSearchResponse(
                items,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    public record ProductSummary(
            Integer id,
            String productName,
            String productDescription,
            Double productPrice,
            int quantity,
            boolean productStatus,
            String categoryKey,
            String categoryLabel,
            String imageUrl
    ) {
    }

    public record ProductSearchResponse(
            List<ProductSummary> items,
            int page,
            int size,
            long totalItems,
            int totalPages
    ) {
    }
}

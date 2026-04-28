package com.phuoctan.service;

import com.phuoctan.entity.Product;
import com.phuoctan.entity.ProductCategory;
import com.phuoctan.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(ProductCategory category){
        return productRepository.findByProductCategory(category);
    }

    public Page<Product> findByCategoryPage(ProductCategory category, int page, int size, Double min, Double max, String keyword, String sort) {
        Sort sortObj = switch (sort) {
            case "price_asc" -> Sort.by("productPrice").ascending();
            case "price_desc" -> Sort.by("productPrice").descending();
            default -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productRepository.findByProductCategory(category, pageable, keyword, min, max);
    }

    public Page<Product> searchProducts(int page, int size, String keyword, String sort) {
        Sort sortObj = switch (sort) {
            case "price_asc" -> Sort.by("productPrice").ascending();
            case "price_desc" -> Sort.by("productPrice").descending();
            case "name_asc" -> Sort.by("productName").ascending();
            case "name_desc" -> Sort.by("productName").descending();
            default -> Sort.by("id").descending();
        };

        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productRepository.searchProducts(pageable, keyword == null ? "" : keyword.trim());
    }


    public void createProduct(Product product) {
        productRepository.save(product);
    }
}

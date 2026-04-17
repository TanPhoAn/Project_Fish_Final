package com.phuoctan.service;

import com.phuoctan.entity.Product;
import com.phuoctan.entity.ProductCategory;
import com.phuoctan.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Product> findByCategoryPage(ProductCategory category, int page, int size, Double min, Double max, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByProductCategory(category, pageable, keyword, min, max);
    }

}

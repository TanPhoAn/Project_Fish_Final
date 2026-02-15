package com.phuoctan.service;

import com.phuoctan.entity.Product;
import com.phuoctan.entity.ProductCategory;
import com.phuoctan.repository.ProductRepository;
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

}

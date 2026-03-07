package com.phuoctan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;


import java.util.Objects;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String productName;
    private String productDescription;
    private long productPrice;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    private String productImage;
    //@Column(nullable = false)
    private boolean productStatus = true;

    public Product() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public boolean getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(productName, product.productName) && Objects.equals(productDescription, product.productDescription) && Objects.equals(productPrice, product.productPrice) && Objects.equals(productCategory, product.productCategory) && Objects.equals(productImage, product.productImage) && Objects.equals(productStatus, product.productStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productDescription, productPrice, productCategory, productImage, productStatus);
    }

    public Product(String productName, String productDescription, long productPrice,  ProductCategory productCategory, boolean productStatus) {
        this.productPrice = productPrice;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productStatus = productStatus;
    }


}

package com.phuoctan.repository;

import com.phuoctan.entity.Product;
import com.phuoctan.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
//import java.awt.print.Pageable;
import java.util.List;
//   JPA
//    |
//    ---PagingAndSortingRepository
//
//        --> CrudRepository
// ko cần anno @repostitory vì extends JPA -> spring quets package -> tìm th nào extend jpa -> repos



public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductCategory(ProductCategory category);
    //Page<Product> findByCategory(ProductCategory category, Pageable pageable);
    Page<Product> findByProductCategory(ProductCategory category, Pageable pageable);


    @Query("""
    SELECT p FROM Product p
    WHERE p.productCategory = :category
    AND (:keyword = '' OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')))
    AND (:min IS NULL OR p.productPrice >= :min)
    AND (:max IS NULL OR p.productPrice <= :max)
    
""")
    Page<Product> findByProductCategory(ProductCategory category, Pageable pageable, String keyword, Double min, Double max);
}

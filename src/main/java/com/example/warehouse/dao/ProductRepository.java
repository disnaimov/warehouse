package com.example.warehouse.dao;

import com.example.warehouse.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 * Repository interface for {@link Product} class.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Product findByArticle(UUID article);
    @Query("SELECT p.name FROM Product p WHERE p.id = :productId")
    String findNameById(@Param("productId") UUID productId);
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}

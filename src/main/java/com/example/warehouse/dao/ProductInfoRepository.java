package com.example.warehouse.dao;

import com.example.warehouse.entities.ProductInfo;
import org.apache.http.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, UUID> {
    List<ProductInfo> findAllById(UUID productId);
}

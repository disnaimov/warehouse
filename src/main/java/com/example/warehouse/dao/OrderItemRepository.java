package com.example.warehouse.dao;

import com.example.warehouse.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    OrderItem findOrderItemByProductId(UUID id);
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.order.id = :orderId")
    List<OrderItem> findAllByOrderIdWithProducts(UUID orderId);
    List<OrderItem> findAllByOrderId(UUID id);
    List<OrderItem> findByOrderId(UUID id);
}

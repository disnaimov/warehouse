package com.example.warehouse.dao;

import com.example.warehouse.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query(nativeQuery = true, value = "SELECT o.id AS order_id, p.product_name, c.login " +
            "FROM orders o " +
            "JOIN order_products op ON o.id = op.order_id " +
            "JOIN products p ON op.product_id = p.id " +
            "JOIN customers c ON o.customer_id = c.id")
    List<Object[]> getAllOrderInfoWithProducts();
}

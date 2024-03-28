package com.example.warehouse.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Table(name = "products")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 7114432016011577607L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "article", nullable = false, unique = true)
    private String article;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "last_quantity_update", nullable = false)
    private Timestamp lastQuantityUpdate;

    @Column(name = "creation_date", nullable = false)
    @CreationTimestamp
    private Timestamp created;
}
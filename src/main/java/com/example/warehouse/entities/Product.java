package com.example.warehouse.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Table(name = "products")
@Getter
@Setter
@Entity
public class Product {
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
    private Timestamp created;
}
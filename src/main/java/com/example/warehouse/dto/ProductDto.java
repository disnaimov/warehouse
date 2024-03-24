package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private UUID id;

    private String name;

    private String article;

    private String description;

    private String category;

    private int price;

    private int quantity;

    private Timestamp lastQuantityUpdate;

    private Timestamp created;
}

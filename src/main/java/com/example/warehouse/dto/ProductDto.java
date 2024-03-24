package com.example.warehouse.dto;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto implements Serializable {
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

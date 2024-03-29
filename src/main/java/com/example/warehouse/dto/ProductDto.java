package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6163089060674642436L;

    private UUID id;

    private String name;

    private String article;

    private String description;

    private String category;

    private int price;

    private int quantity;

    private Timestamp lastQuantityUpdate;

    private LocalDate created;
}

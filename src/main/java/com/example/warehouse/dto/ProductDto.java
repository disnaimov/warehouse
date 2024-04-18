package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 * DTO class for {@link com.example.warehouse.entities.Product} class.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6163089060674642436L;

    private UUID id;

    private String name;

    private UUID article;

    private String description;

    private String category;

    private BigDecimal price;

    private int quantity;

    private Timestamp lastQuantityUpdate;

    private LocalDate created;
}

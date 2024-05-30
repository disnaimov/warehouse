package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 *  DTO class for updating {@link com.example.warehouse.entities.Product} class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5071536335396577564L;
    private UUID id;
    private String name;
    private UUID article;
    private String description;
    private String category;
    private BigDecimal price;
    private int quantity;
}

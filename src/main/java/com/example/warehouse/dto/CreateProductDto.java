package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 *  DTO class for creating {@link com.example.warehouse.entities.Product} class.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -128320876775080239L;
    private String name;
    private UUID article;
    private String description;
    private String category;
    private BigDecimal price;
    private int quantity;
}

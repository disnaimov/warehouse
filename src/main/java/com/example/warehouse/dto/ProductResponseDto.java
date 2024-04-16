package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 *  DTO class for the response method of getting a product by id {@link com.example.warehouse.entities.Product} class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -881207447769697757L;
    private UUID id;
    private String name;
    private String description;
    private String category;
    private int price;
    private int quantity;
    private Timestamp lastQuantityUpdate;
    private LocalDate created;
}

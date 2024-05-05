package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseWithCurrencyDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -4385383432376820850L;
    private UUID id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private int quantity;
    private Long lastQuantityUpdate;
    private Long created;
    private String currency;
}

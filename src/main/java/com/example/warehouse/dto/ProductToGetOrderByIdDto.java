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
public class ProductToGetOrderByIdDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 8863486396645324938L;
    private UUID productId;
    private String name;
    private int quantity;
    private BigDecimal price;
}

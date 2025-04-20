package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderGetByIdDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 2845819198715175736L;
    private UUID orderId;
    private List<ProductToGetOrderByIdDto> products;
    private BigDecimal totalPrice;
}

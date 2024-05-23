package com.example.warehouse.entities;

import com.example.warehouse.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 9220816928357879992L;
    private UUID id;
    private CustomerInfo customer;
    private OrderStatus status;
    private String deliveryAddress;
    private Integer quantity;
}

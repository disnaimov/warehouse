package com.example.warehouse.dto;

import com.example.warehouse.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OrderDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7076465327547828864L;
    private UUID id;
    @JsonProperty("customer")
    private Long customerId;
    private OrderStatus status;
    @JsonProperty("address")
    private String deliveryAddress;
}

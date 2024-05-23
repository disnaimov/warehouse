package com.example.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 8297962574164638893L;
    private String deliveryAddress;
    @JsonProperty("products")
    private List<ProductToOrderDto> products;
}

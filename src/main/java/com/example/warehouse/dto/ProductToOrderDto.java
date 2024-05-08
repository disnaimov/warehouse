package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductToOrderDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1118989370085035537L;
    private UUID id;
    private int quantity;
}

package com.example.warehouse.dto;

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
public class PatchOrderDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -3328829913139477888L;
    private List<ProductToOrderDto> patchOrders;
}

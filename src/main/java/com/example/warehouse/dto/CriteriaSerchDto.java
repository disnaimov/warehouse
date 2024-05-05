package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaSerchDto<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -5707368793274553841L;
    private String field;
    private T value;
    private String operation;

}

package com.example.warehouse.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 7649791715932093897L;
    private Long id;
    private String accountNumber;
    private String email;
    private String inn;
}

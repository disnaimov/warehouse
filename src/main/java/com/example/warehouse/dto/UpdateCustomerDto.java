package com.example.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateCustomerDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 263585377895761607L;
    private Long id;
    private String login;
    private String email;
    @JsonProperty("active")
    private boolean isActive;
}

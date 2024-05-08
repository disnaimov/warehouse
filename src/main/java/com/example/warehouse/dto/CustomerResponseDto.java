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
public class CustomerResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -4091288412277824932L;
    private Long id;
    private String login;
    private String email;
    @JsonProperty("active")
    private boolean isActive;
}

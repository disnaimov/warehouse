package com.example.warehouse.dto;

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
public class CustomerDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8920648318663759753L;
    private Long id;
    private String login;
    private String email;
    private boolean isActive;
}

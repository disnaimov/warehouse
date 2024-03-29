package com.example.warehouse.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class InvalidEntityDataException extends RuntimeException{
        private String message;
        private String errorCode;
        private HttpStatus status;
}

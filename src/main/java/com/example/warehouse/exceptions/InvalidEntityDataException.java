package com.example.warehouse.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 * Custom exception class, necessary for a clear API response in case of validation errors
 * Contains parameters message, error code, status
 */
@AllArgsConstructor
@Getter
public class InvalidEntityDataException extends RuntimeException{
        private String message;
        private String errorCode;
        private HttpStatus status;
}

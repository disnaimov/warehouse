package com.example.warehouse.hanlders;

import com.example.warehouse.exceptions.InvalidEntityDataException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ProductServiceExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = InvalidEntityDataException.class)
    protected ResponseEntity<Object> handleInvalidEntityDataException(InvalidEntityDataException ex) {

        ExceptionHandlerResponse exceptionHandlerResponse = new ExceptionHandlerResponse(ex.getMessage(), ex.getErrorCode(), ex.getStatus());
        return new ResponseEntity<>(exceptionHandlerResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("user passed incorrect value");
        ExceptionHandlerResponse exceptionHandlerResponse = new ExceptionHandlerResponse(ex.getMessage(), "INCORRECT_ARGUMENT", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionHandlerResponse, HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ExceptionHandlerResponse{
        private String message;
        private String errorCode;
        private HttpStatus status;
    }
}
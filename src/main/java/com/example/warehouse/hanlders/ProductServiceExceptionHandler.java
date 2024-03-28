package com.example.warehouse.hanlders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ProductServiceExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = { PropertyValueException.class})
    protected ResponseEntity<Object> handlePropertyValueException(PropertyValueException ex, WebRequest request) {
        AwesomeException awesomeException = new AwesomeException("Переданные значение для " + ex.getPropertyName() + " является некорректным", HttpStatus.UNPROCESSABLE_ENTITY.toString());
        log.error("У сущности " + ex.getEntityName() + " для поля " + ex.getPropertyName() + " переданно некорректное значение");
        return new ResponseEntity<>(awesomeException, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = PSQLException.class)
    protected ResponseEntity<Object> handlePSQLException(PSQLException ex, WebRequest request) {
        AwesomeException awesomeException = new AwesomeException("Переданное значение для поля артикул является некорректным", HttpStatus.UNPROCESSABLE_ENTITY.toString());
        log.error(ex.getMessage());
        return new ResponseEntity<>(awesomeException, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(value = NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        AwesomeException awesomeException = new AwesomeException("Ошибка, отсутствует переданное значение, проверьте наличие и правильность передаваемого значения", HttpStatus.UNPROCESSABLE_ENTITY.toString());
        log.error(ex.getMessage());
        return new ResponseEntity<>(awesomeException, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        AwesomeException awesomeException = new AwesomeException(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.toString());
        log.error(ex.getMessage());
        return new ResponseEntity<>(awesomeException, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AwesomeException{
        private String message;
        private String error_code;
    }
}
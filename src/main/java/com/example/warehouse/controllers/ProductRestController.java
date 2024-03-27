package com.example.warehouse.controllers;

import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.dto.ProductResponseDto;
import com.example.warehouse.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Validated ProductDto productDto) {
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(productService.create(productDto));
        ProductResponseDto productResponseDto = new ProductResponseDto(CREATED.value(), productDtos);

        return new ResponseEntity<>(productResponseDto, CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ProductResponseDto> update(@RequestBody @Validated ProductDto productDto) {
        productService.update(productDto);
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(productDto);

        ProductResponseDto productResponseDto = new ProductResponseDto(OK.value(), productDtos);

        return new ResponseEntity<>(productResponseDto, OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ProductResponseDto> getAll() {
        List<ProductDto> productDtos = productService.getAll();
        ProductResponseDto productResponseDto = new ProductResponseDto(OK.value(), productDtos);

        return new ResponseEntity<>(productResponseDto, OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductResponseDto> getById(@PathVariable("id") UUID id){
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(productService.getById(id));

        ProductResponseDto productResponseDto = new ProductResponseDto(OK.value(), productDtos);

        return new ResponseEntity<>(productResponseDto, OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProductResponseDto> deleteById(@PathVariable("id") UUID id) {
        productService.removeById(id);
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(productService.getById(id));

        ProductResponseDto productResponseDto = new ProductResponseDto(OK.value(), productDtos);

        return new ResponseEntity<>(productResponseDto, OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<ProductResponseDto> deleteAll() {
        productService.deleteAll();
        ProductResponseDto productResponseDto = new ProductResponseDto(OK.value(), null);

        return new ResponseEntity<>(productResponseDto, OK);
    }
}

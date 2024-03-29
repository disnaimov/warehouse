package com.example.warehouse.controllers;

import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.exceptions.InvalidEntityDataException;
import com.example.warehouse.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) throws InvalidEntityDataException {
        return new ResponseEntity<>(productService.create(productDto), CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.update(productDto), OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProductDto>> getAll(@RequestParam (required = false, defaultValue = "0") int page,
                                                   @RequestParam (required = false, defaultValue = "20") int size) {

        return new ResponseEntity<>(productService.getAll(PageRequest.of(page, size)), OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductDto> getById(@PathVariable("id") UUID id){
        return new ResponseEntity<>(productService.getById(id), OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteById(@PathVariable("id") UUID id) {
        productService.removeById(id);
        return new ResponseEntity<>(OK);
    }
}

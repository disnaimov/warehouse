package com.example.warehouse.controllers;

import com.example.warehouse.dto.*;
import com.example.warehouse.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 * Product rest controller class
 * Used {@link ProductService} class. {@link ProductDto} class.
 */
@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    /**
     * Create a new product with parameters received from the user using product service
     * @param createProductDto - product DTO received from the user
     * @return new Response Entity with the return value of the create method and the created status
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UUID> create(@RequestBody CreateProductDto createProductDto) {
        return new ResponseEntity<>(productService.create(createProductDto), CREATED);
    }

    /**
     *Update product using user-supplied parameters
     * @param updateProductDto - product DTO received from the user
     * @return new Response Entity with the return value of the update method and the ok status
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ProductResponseDto> update(@RequestBody UpdateProductDto updateProductDto) {
        return new ResponseEntity<>(productService.update(updateProductDto), OK);
    }

    /**
     * Get all product method
     * @param page - number of pages, needed for pagination
     * @param size - number of elements per page, needed for pagination
     * @return new Response Entity with the return value of the get all method and the ok status
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProductResponseDto>> getAll(@RequestParam (required = false, defaultValue = "0") int page,
                                                   @RequestParam (required = false, defaultValue = "20") int size) {

        return new ResponseEntity<>(productService.getAll(PageRequest.of(page, size)), OK);
    }

    /**
     * Get by id method
     * @param id - id parameter received from the user
     * @return new Response Entity with the return value of the get by id method and the ok status
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductResponseDto> getById(@PathVariable("id") UUID id){
        return new ResponseEntity<>(productService.getById(id), OK);
    }

    /**
     * Delete by id method
     * @param id - id parameter received from the user
     * @return new Response Entity with the ok status
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteById(@PathVariable("id") UUID id) {
        productService.removeById(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}

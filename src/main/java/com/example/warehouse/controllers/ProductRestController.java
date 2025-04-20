package com.example.warehouse.controllers;

import com.example.warehouse.dto.CreateProductDto;
import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.dto.ProductResponseDto;
import com.example.warehouse.dto.ProductResponseWithCurrencyDto;
import com.example.warehouse.dto.UpdateProductDto;
import com.example.warehouse.search.criteria.SearchCriteria;
import com.example.warehouse.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

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
     *
     * @param createProductDto - product DTO received from the user
     * @return new Response Entity with the return value of the create method and the created status
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UUID> create(@RequestBody CreateProductDto createProductDto) {
        return new ResponseEntity<>(productService.create(createProductDto), CREATED);
    }

    /**
     * Update product using user-supplied parameters
     *
     * @param updateProductDto - product DTO received from the user
     * @return new Response Entity with the return value of the update method and the ok status
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ProductResponseDto update(@RequestBody UpdateProductDto updateProductDto) {
        return productService.update(updateProductDto);
    }

    /**
     * Get all product method
     *
     * @param page - number of pages, needed for pagination
     * @param size - number of elements per page, needed for pagination
     * @return new Response Entity with the return value of the get all method and the ok status
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ProductResponseWithCurrencyDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "20") int size,
                                                       @RequestHeader(required = false) String currency) {

        return productService.getAll(PageRequest.of(page, size), currency);
    }

    /**
     * Get by id method
     *
     * @param id - id parameter received from the user
     * @return new Response Entity with the return value of the get by id method and the ok status
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductResponseWithCurrencyDto getById(@PathVariable("id") UUID id, @RequestHeader(required = false) String currency) {
        return productService.getById(id, currency);
    }

    /**
     * Delete by id method
     *
     * @param id - id parameter received from the user
     * @return new Response Entity with the ok status
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteById(@PathVariable("id") UUID id) {
        productService.removeById(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cr")
    public ResponseEntity<List<ProductResponseDto>> criterialSearch(@RequestParam (required = false, defaultValue = "0") int page,
                                                                   @RequestParam (required = false, defaultValue = "20") int size,
                                                                   @RequestBody List<CriteriaSerchDto> criteriaDto) {


        return new ResponseEntity<>(productService.criterialSearch(PageRequest.of(page, size), criteriaDto), OK) ;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("productId") UUID productId) {
        try {
            productService.uploadFile(file, productId);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{productId}")
    public void downloadFilesForProduct(@PathVariable UUID productId, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"product_files.zip\"");

            try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
                productService.downloadFilesForProduct(productId, zos);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to create zip file: " + e.getMessage());
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to download files: " + e.getMessage());
        }
    }
}

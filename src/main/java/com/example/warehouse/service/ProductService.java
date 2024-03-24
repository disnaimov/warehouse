package com.example.warehouse.service;

import com.example.warehouse.dao.ProductRepository;
import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entities.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper mapper;

    public ProductDto create(ProductDto productDto) {
        log.info("Saving product");
        log.debug("Saving product {}", productDto.toString());
        Date date = new Date();

        Product product = mapper.map(productDto, Product.class);
        product.setLastQuantityUpdate(new Timestamp(date.getTime()));
        product = productRepository.save(product);
        ProductDto saved = mapper.map(product, ProductDto.class);

        log.info("Product saved");
        log.debug("Product saved {}", saved.toString());
        return saved;
    }

    public ProductDto update(ProductDto productDto){
        log.info("Updating product");
        log.debug("Updating product {}", productDto.toString());


            Product product = productRepository.findById(productDto.getId()).orElseThrow();
            if (product.getQuantity() != productDto.getQuantity()) {
                Date currentDate = new Date();
                product.setLastQuantityUpdate(new Timestamp(currentDate.getTime()));
            }

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());

            product = productRepository.save(product);
            ProductDto updated = mapper.map(product, ProductDto.class);


        log.info("Product updated");
        log.debug("Product updated {}", updated.toString());
        return updated;
    }

    public void removeById(UUID id){
        log.info("Removal product by id");
        log.debug("Removal product by id {}", id);
        productRepository.delete(productRepository.findById(id).orElseThrow());
        log.info("Product by id removed");
        log.debug("Product by id removed {}", productRepository.findById(id));
    }

    public List<ProductDto> getAll() {
        log.info("getting all products");
        log.debug("getting all products");
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product p: products){
            productDtos.add(mapper.map(p, ProductDto.class));
        }

        log.info("All products received");
        log.debug("All products received");
        return productDtos;
    }

    public ProductDto getById(UUID id) {
        log.info("Getting product by id");
        log.debug("Getting product by id {}", id);
        return mapper.map(productRepository.findById(id), ProductDto.class);
    }
}

package com.example.warehouse.service;

import com.example.warehouse.dao.ProductRepository;
import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entities.Product;
import com.example.warehouse.exceptions.InvalidEntityDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
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

    private void validation(ProductDto productDto) {
        if (productRepository.findByArticle(productDto.getArticle()) != null) {
            log.error("received a non-unique article");
            throw new InvalidEntityDataException("Указанный артикул уже существует", "INCORRECT_ARTICLE", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getArticle() == null || productDto.getArticle().isBlank()) {
            log.error("received incorrect article");
            throw new InvalidEntityDataException("Некорректный артикул: Проверьте правильность ввода и повторите попытку.", "INCORRECT_ARTICLE", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getCategory() == null || productDto.getCategory().isBlank()) {
            log.error("received incorrect category");
            throw new InvalidEntityDataException("Некорректная категория: Проверьте правильность ввода и повторите попытку.", "INCORRECT_CATEGORY", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getDescription() == null || productDto.getDescription().isBlank()) {
            log.error("received incorrect description");
            throw new InvalidEntityDataException("Некорректное описание: Проверьте правильность ввода и повторите попытку.", "INCORRECT_DESCRIPTION", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getPrice() <= 0) {
            log.error("received incorrect price, <= 0");
            throw new InvalidEntityDataException("Некорректная цена: Проверьте правильность ввода и повторите попытку.", "INCORRECT_PRICE", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getQuantity() <= 0) {
            log.error("received incorrect quantity, <= 0");
            throw new InvalidEntityDataException("Некорректное количество: Проверьте правильность ввода и повторите попытку.", "INCORRECT_QUANTITY", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    public ProductDto create(ProductDto productDto) throws InvalidEntityDataException {
        log.info("Saving product");
        log.debug("Saving product {}", productDto.toString());

            validation(productDto);

            Date date = new Date();
            Product product = mapper.map(productDto, Product.class);
            product.setCreated(LocalDate.now());
            product.setLastQuantityUpdate(new Timestamp(date.getTime()));
            product = productRepository.save(product);
            ProductDto saved = mapper.map(product, ProductDto.class);

            log.info("Product saved");
            log.debug("Product saved {}", saved.toString());
            return saved;
    }

    @Transactional
    public ProductDto update(ProductDto productDto){
        log.info("Updating product");
        log.debug("Updating product {}", productDto.toString());

            validation(productDto);

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

    @Transactional
    public void removeById(UUID id){
        log.info("Removal product by id");
        log.debug("Removal product by id {}", id);
        productRepository.delete(productRepository.findById(id).orElseThrow());
        log.info("Product by id removed");
        log.debug("Product by id removed {}", productRepository.findById(id));
    }

    @Transactional
    public List<ProductDto> getAll(PageRequest pageRequest) {
        log.info("getting all products");
        log.debug("getting all products");

        List<Product> products = productRepository.findAll(pageRequest).getContent();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product p: products){
            productDtos.add(mapper.map(p, ProductDto.class));
        }

        log.info("All products received");
        log.debug("All products received");
        return productDtos;
    }

    @Transactional
    public ProductDto getById(UUID id) {
        log.info("Getting product by id");
        log.debug("Getting product by id {}", id);
        return mapper.map(productRepository.findById(id), ProductDto.class);
    }
}

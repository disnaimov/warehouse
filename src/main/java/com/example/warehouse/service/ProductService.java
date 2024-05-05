package com.example.warehouse.service;

import com.example.warehouse.annotation.MethodExecutionTime;
import com.example.warehouse.dao.ProductRepository;
import com.example.warehouse.dto.*;
import com.example.warehouse.entities.Product;
import com.example.warehouse.exceptions.InvalidEntityDataException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dmitriy
 * @version 1.0
 * @since 1.0
 * Product service class
 * Used {@link ProductRepository} class. {@link ModelMapper} class. {@link InvalidEntityDataException} class.
 * {@link CreateProductDto} class. {@link com.example.warehouse.dto.UpdateProductDto} class. {@link ProductResponseDto} class.
 * Has validation and CRUD methods
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository productRepository;

    private final ModelMapper mapper;

    /**
     * Validation method, checks input DTO
     * @param productDto - product DTO received from the user
     */
    private void validation(ProductDto productDto) {
        if (productRepository.findByArticle(productDto.getArticle()) != null) {
            log.error("received a non-unique article");
            throw new InvalidEntityDataException("Указанный артикул уже существует", "INCORRECT_ARTICLE", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getArticle() == null) {
            log.error("received incorrect article");
            throw new InvalidEntityDataException("Некорректный артикул: Проверьте правильность ввода и повторите попытку.", "INCORRECT_ARTICLE", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getName() == null || productDto.getName().isBlank()) {
            throw new InvalidEntityDataException("Неккоректное название: Проверьте правильность ввода и повторите попытку.", "INCORRECT_NAME", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getCategory() == null || productDto.getCategory().isBlank()) {
            log.error("received incorrect category");
            throw new InvalidEntityDataException("Некорректная категория: Проверьте правильность ввода и повторите попытку.", "INCORRECT_CATEGORY", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getDescription() == null || productDto.getDescription().isBlank()) {
            log.error("received incorrect description");
            throw new InvalidEntityDataException("Некорректное описание: Проверьте правильность ввода и повторите попытку.", "INCORRECT_DESCRIPTION", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("received incorrect price, <= 0");
            throw new InvalidEntityDataException("Некорректная цена: Проверьте правильность ввода и повторите попытку.", "INCORRECT_PRICE", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (productDto.getQuantity() <= 0) {
            log.error("received incorrect quantity, <= 0");
            throw new InvalidEntityDataException("Некорректное количество: Проверьте правильность ввода и повторите попытку.", "INCORRECT_QUANTITY", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * method for creating a new product based on users dto
     * @param createProductDto - product received from the user
     * @return new Product DTO
     */
    @Transactional
    public UUID create(CreateProductDto createProductDto) {
        log.info("Saving product");
        log.debug("Saving product {}", createProductDto.toString());

        ProductDto productDto = mapper.map(createProductDto, ProductDto.class);


        long dATA = System.currentTimeMillis()/1000;
        Date date = new Date(dATA);
        Timestamp timestamp = new Timestamp(dATA);
        log.info(date.toString());
        log.info(timestamp.toString());

        validation(productDto);


            Product product = mapper.map(productDto, Product.class);
            product.setCreated(dATA);
            product.setLastQuantityUpdate(dATA);
            product = productRepository.save(product);
            productDto = mapper.map(product, ProductDto.class);



        log.info("Product saved");
        log.debug("Product saved {}", productDto.toString());
        return productDto.getId();
    }

    /**
     * product update method based on users dto
     * @param updateProductDto - product received from the user
     * @return Updated Product DTO
     */
    @Transactional
    public ProductResponseDto update(UpdateProductDto updateProductDto){
        log.info("Updating product");
        log.debug("Updating product {}", updateProductDto.toString());

        ProductDto productDto = mapper.map(updateProductDto, ProductDto.class);

            validation(productDto);

            Product product = productRepository.findById(productDto.getId()).orElseThrow();
            if (product.getQuantity() != productDto.getQuantity()) {
                product.setLastQuantityUpdate(System.currentTimeMillis()/1000);
            }

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());

            product = productRepository.save(product);
            ProductResponseDto productResponseDto = mapper.map(product, ProductResponseDto.class);


        log.info("Product updated");
        log.debug("Product updated {}", product);
        return productResponseDto;
    }

    /**
     * deleting a product by id
     * @param id - id parameter received from the user
     */
    @Transactional
    public void removeById(UUID id){
        log.info("Removal product by id");
        log.debug("Removal product by id {}", id);
        if (productRepository.findById(id).isPresent()) {
            productRepository.delete(productRepository.findById(id).orElseThrow());
        }
        else throw new InvalidEntityDataException("Ошибка: указанный id не существует", "INCORRECT_ID", HttpStatus.NOT_FOUND);
        log.info("Product by id removed");
        log.debug("Product by id removed {}", productRepository.findById(id));
    }

    /**
     * returns the specified number of products,
     * if the page size and number of items parameters are not specified, the default parameters will be used
     * @param pageRequest contains the number of pages and elements per page. required for pagination
     * @return List Product DTOs
     */
    @Transactional
    @MethodExecutionTime
    public List<ProductResponseDto> getAll(PageRequest pageRequest) {
        log.info("getting all products");
        log.debug("getting all products");

        List<Product> products = productRepository.findAll(pageRequest).getContent();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Product p: products){
            productResponseDtos.add(mapper.map(p, ProductResponseDto.class));
        }

        log.info("All products received");
        log.debug("All products received");
        return productResponseDtos;
    }

    /**
     *get by id method
     * @param id - id parameter received from the user
     * @return Product DTO by user specified id
     */
    @Transactional
    public ProductResponseDto getById(UUID id) {
        log.info("Getting product by id");
        log.debug("Getting product by id {}", id);

        if (productRepository.findById(id).isPresent()) {
            return mapper.map(productRepository.findById(id), ProductResponseDto.class);
        }
        else throw new InvalidEntityDataException("Ошибка: указанный id не существует", "INCORRECT_ID", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public List<ProductResponseDto> criterialSearch(PageRequest pageRequest, List<CriteriaSerchDto> criteriaDto) {
        log.info("criterial search");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);

        Predicate predicate = builder.conjunction();

        for (CriteriaSerchDto d : criteriaDto) {
            Expression<? extends Comparable> fieldExpression = root.get(d.getField());
            Comparable value = (Comparable) d.getValue();

            switch (d.getOperation()) {
                case ">=":
                case "GRATER_THAN_OR_EQ":
                    predicate = builder.and(predicate, builder.greaterThanOrEqualTo(fieldExpression, value));
                    break;
                case "<=":
                case "LESS_THAN_OR_EQ":
                    predicate = builder.and(predicate, builder.lessThanOrEqualTo(fieldExpression, value));
                    break;
                case "=":
                case "EQUALS":
                    predicate = builder.and(predicate, builder.equal(fieldExpression, value));
                    break;
                case "~":
                case "LIKE":
                    Predicate condition;
                    if (fieldExpression.getJavaType() == BigDecimal.class || fieldExpression.getJavaType() == Integer.class) {
                        Comparable lowerValue = value instanceof BigDecimal ? ((BigDecimal) value).subtract(BigDecimal.TEN) : (int) value - 10;
                        Comparable upperValue = value instanceof BigDecimal ? ((BigDecimal) value).add(BigDecimal.TEN) : (int) value + 10;
                        condition = builder.between(fieldExpression, lowerValue, upperValue);
                    } else {
                        condition = builder.like(root.get(d.getField()).as(String.class), "%" + d.getValue() + "%");
                    }
                    predicate = builder.and(predicate, condition);
                    break;
            }
        }

        query.where(predicate);

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        typedQuery.setMaxResults(pageRequest.getPageSize());

        List<Product> products = typedQuery.getResultList();
        List<ProductResponseDto> productResponseDtos = products.stream()
                .map(p -> mapper.map(p, ProductResponseDto.class))
                .collect(Collectors.toList());

        return productResponseDtos;
    }
}

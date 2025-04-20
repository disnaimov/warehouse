package com.example.warehouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.example.warehouse.annotation.MethodExecutionTime;
import com.example.warehouse.dao.ProductInfoRepository;
import com.example.warehouse.dao.ProductRepository;
import com.example.warehouse.dto.CreateProductDto;
import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.dto.ProductResponseDto;
import com.example.warehouse.dto.ProductResponseWithCurrencyDto;
import com.example.warehouse.dto.UpdateProductDto;
import com.example.warehouse.entities.Currency;
import com.example.warehouse.entities.Product;
import com.example.warehouse.entities.ProductInfo;
import com.example.warehouse.exceptions.InvalidEntityDataException;
import com.example.warehouse.provider.ExchangeRateProvider;
import com.example.warehouse.search.ProductSpecification;
import com.example.warehouse.search.criteria.SearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.FileEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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

    private final ProductRepository productRepository;
    private final ModelMapper mapper;
    private final ProductInfoRepository productInfoRepository;
    private final AmazonS3 s3Client;
    private final ExchangeRateProvider exchangeRateProvider;

    /**
     * Validation method, checks input DTO
     *
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

        if (productDto.getQuantity() < 0) {
            log.error("received incorrect quantity, = 0");
            throw new InvalidEntityDataException("Некорректное количество: Проверьте правильность ввода и повторите попытку.", "INCORRECT_QUANTITY", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * method for creating a new product based on users dto
     *
     * @param createProductDto - product received from the user
     * @return new Product DTO
     */
    @Transactional
    public UUID create(CreateProductDto createProductDto) {
        log.info("Saving product");
        log.debug("Saving product {}", createProductDto.toString());

        ProductDto productDto = mapper.map(createProductDto, ProductDto.class);


        long dATA = System.currentTimeMillis() / 1000;
        Date date = new Date(dATA);
        Timestamp timestamp = new Timestamp(dATA);
        log.info(date.toString());
        log.info(timestamp.toString());

        validation(productDto);

            Product product = mapper.map(productDto, Product.class);
            product.setCreated(dATA);
            product.setLastQuantityUpdate(dATA);
            if (productDto.getQuantity() == 0) {
                product.setAvailable(false);
            } else {
                product.setAvailable(true);
            }
            product = productRepository.save(product);
            productDto = mapper.map(product, ProductDto.class);

        log.info("Product saved");
        log.debug("Product saved {}", productDto.toString());
        return productDto.getId();
    }

    /**
     * product update method based on users dto
     *
     * @param updateProductDto - product received from the user
     * @return Updated Product DTO
     */
    @Transactional
    public ProductResponseDto update(UpdateProductDto updateProductDto) {
        log.info("Updating product");
        log.debug("Updating product {}", updateProductDto.toString());

        ProductDto productDto = mapper.map(updateProductDto, ProductDto.class);

        validation(productDto);

        Product product = productRepository.findById(productDto.getId()).orElseThrow();
        if (product.getQuantity() != productDto.getQuantity()) {
            product.setLastQuantityUpdate(System.currentTimeMillis() / 1000);
        }

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());

            if (productDto.getQuantity() == 0) {
                product.setAvailable(false);
            } else {
                product.setAvailable(true);
            }

            product = productRepository.save(product);
            ProductResponseDto productResponseDto = mapper.map(product, ProductResponseDto.class);

        log.info("Product updated");
        log.debug("Product updated {}", product);
        return productResponseDto;
    }

    /**
     * deleting a product by id
     *
     * @param id - id parameter received from the user
     */
    @Transactional
    public void removeById(UUID id) {
        log.info("Removal product by id");
        log.debug("Removal product by id {}", id);
        if (productRepository.findById(id).isPresent()) {
            productRepository.delete(productRepository.findById(id).orElseThrow());
        } else
            throw new InvalidEntityDataException("Ошибка: указанный id не существует", "INCORRECT_ID", HttpStatus.NOT_FOUND);
        log.info("Product by id removed");
        log.debug("Product by id removed {}", productRepository.findById(id));
    }

    /**
     * returns the specified number of products,
     * if the page size and number of items parameters are not specified, the default parameters will be used
     *
     * @param pageRequest contains the number of pages and elements per page. required for pagination
     * @return List Product DTOs
     */
    @Transactional
    public List<ProductResponseWithCurrencyDto> getAll(PageRequest pageRequest, String currency) {
        log.info("getting all products");
        log.debug("getting all products");

        List<Product> products = productRepository.findAll(pageRequest).getContent();
        List<ProductResponseWithCurrencyDto> productResponseDtos = new ArrayList<>();

        BigDecimal multiply = exchangeRateProvider.getExchangeRate(Currency.fromString(currency));

        for (Product p : products) {
            productResponseDtos.add(mapper.map(p, ProductResponseWithCurrencyDto.class));
        }

        for (ProductResponseWithCurrencyDto p : productResponseDtos) {
            p.setPrice(p.getPrice().multiply(multiply));
            p.setCurrency(Currency.fromString(currency));
        }


        log.info("All products received");
        log.debug("All products received");
        return productResponseDtos;
    }

    /**
     * get by id method
     *
     * @param id - id parameter received from the user
     * @return Product DTO by user specified id
     */
    @Transactional
    public ProductResponseWithCurrencyDto getById(UUID id, String currency) {
        log.info("Getting product by id");
        log.debug("Getting product by id {}", id);
        if (productRepository.findById(id).isPresent()) {
            ProductResponseWithCurrencyDto productResponseDto = mapper.map(productRepository.findById(id), ProductResponseWithCurrencyDto.class);

            BigDecimal multiply = exchangeRateProvider.getExchangeRate(Currency.fromString(currency));
            productResponseDto.setPrice(productResponseDto.getPrice().multiply(multiply));
            productResponseDto.setCurrency(Currency.fromString(currency));

            return productResponseDto;
        } else
            throw new InvalidEntityDataException("Ошибка: указанный id не существует", "INCORRECT_ID", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public List<ProductResponseDto> criteriaSearch(PageRequest pageRequest, List<SearchCriteria> searchCriteria) {
        log.info("criteria search");

        final ProductSpecification specification = new ProductSpecification(searchCriteria);
        final Page<Product> products = productRepository.findAll(specification, pageRequest);

        return products.getContent().stream()
                .map(product -> mapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    public void uploadFile(MultipartFile file, UUID productId) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        s3Client.putObject("new-bucket-01197bee", fileName, file.getInputStream(), new ObjectMetadata());

        ProductInfo productInfo = new ProductInfo(productId, fileName);
        productInfoRepository.save(productInfo);
    }

    public void downloadFilesForProduct(UUID productId, ZipOutputStream zos) throws IOException {
        List<ProductInfo> productInfos = productInfoRepository.findAllById(productId);

        for (ProductInfo info : productInfos) {
            ZipEntry zipEntry = new ZipEntry(info.getProductName());
            zos.putNextEntry(zipEntry);

            S3Object s3Object = s3Client.getObject("bucketName", String.valueOf(info.getProductFileId()));
            InputStream fileInputStream = s3Object.getObjectContent();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            zos.closeEntry();
            fileInputStream.close();
        }

        zos.finish();
    }

}

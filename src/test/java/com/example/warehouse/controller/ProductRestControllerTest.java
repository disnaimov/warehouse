    package com.example.warehouse.controller;

    import com.example.warehouse.controllers.ProductRestController;
    import com.example.warehouse.dao.ProductRepository;
    import com.example.warehouse.dto.CreateProductDto;
    import com.example.warehouse.dto.ProductDto;
    import com.example.warehouse.dto.ProductResponseDto;
    import com.example.warehouse.dto.UpdateProductDto;
    import com.example.warehouse.service.ProductService;
    import lombok.extern.slf4j.Slf4j;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.modelmapper.ModelMapper;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;

    import java.math.BigDecimal;
    import java.sql.Timestamp;
    import java.time.LocalDate;
    import java.util.Date;
    import java.util.List;
    import java.util.UUID;

    @ExtendWith(MockitoExtension.class)
    @Slf4j
    public class ProductRestControllerTest {
        @Mock
        ProductService productService;
        @Mock
        ProductRepository productRepository;
        @Mock
        ModelMapper mapper;
        @InjectMocks
        ProductRestController productRestController;

        @Test
        void productRestControllerResponseTest() {

            Date date = new Date();

            CreateProductDto createChair = new CreateProductDto();
            createChair.setName("chair");
            createChair.setArticle(UUID.randomUUID());
            createChair.setCategory("category");
            createChair.setDescription("desc");
            createChair.setPrice(BigDecimal.valueOf(10));
            createChair.setQuantity(20);

            UpdateProductDto updateChair = new UpdateProductDto();
            updateChair.setId(UUID.randomUUID());
            updateChair.setName("chair");
            updateChair.setArticle(UUID.randomUUID());
            updateChair.setCategory("category");
            updateChair.setDescription("desc");
            updateChair.setPrice(BigDecimal.valueOf(10));
            updateChair.setQuantity(20);

            CreateProductDto createTv = new CreateProductDto();
            createTv.setName("tv");
            createTv.setArticle(UUID.randomUUID());
            createTv.setCategory("category2");
            createTv.setDescription("descccc");
            createTv.setPrice(BigDecimal.valueOf(100));
            createTv.setQuantity(2000);

            UpdateProductDto updateTv = new UpdateProductDto();
            updateTv.setId(UUID.randomUUID());
            updateTv.setName("tv");
            updateTv.setArticle(UUID.randomUUID());
            updateTv.setCategory("category2");
            updateTv.setDescription("descccc");
            updateTv.setPrice(BigDecimal.valueOf(100));
            updateTv.setQuantity(2000);

            ProductDto chair = new ProductDto();
            chair.setId(UUID.randomUUID());
            chair.setName("chair");
            chair.setArticle(UUID.randomUUID());
            chair.setCategory("category");
            chair.setDescription("desc");
            chair.setPrice(BigDecimal.valueOf(10));
            chair.setQuantity(20);
            chair.setCreated(LocalDate.now());
            chair.setLastQuantityUpdate(new Timestamp(date.getTime()));

            ProductDto tvv = new ProductDto();
            tvv.setId(UUID.randomUUID());
            tvv.setName("tv");
            tvv.setArticle(UUID.randomUUID());
            tvv.setCategory("category2");
            tvv.setDescription("descccc");
            tvv.setPrice(BigDecimal.valueOf(100));
            tvv.setQuantity(2000);
            tvv.setCreated(LocalDate.now());
            tvv.setLastQuantityUpdate(new Timestamp(date.getTime()));

            ResponseEntity<UUID> firstCreateResponse = this.productRestController.create(createTv);
            ResponseEntity<UUID> secondCreateResponse = this.productRestController.create(createChair);

            ResponseEntity<ProductResponseDto> firstGetByIdResponse = this.productRestController.getById(tvv.getId());
            ResponseEntity<ProductResponseDto> secondGetByIdResponse = this.productRestController.getById(chair.getId());

            ResponseEntity<List<ProductResponseDto>> getAllResponse = this.productRestController.getAll(1, 10);

            ResponseEntity<Object> firstRemoveByIdResponse = this.productRestController.deleteById(tvv.getId());
            ResponseEntity<Object> secondRemoveByIdResponse = this.productRestController.deleteById(chair.getId());

            ResponseEntity<ProductResponseDto> firstUpdateResponse = this.productRestController.update(updateTv);
            ResponseEntity<ProductResponseDto> secondUpdateResponse = this.productRestController.update(updateChair);

            Assertions.assertNotNull(firstCreateResponse);
            Assertions.assertNotNull(secondCreateResponse);

            Assertions.assertNotNull(firstGetByIdResponse);
            Assertions.assertNotNull(secondGetByIdResponse);

            Assertions.assertNotNull(getAllResponse);

            Assertions.assertNotNull(firstRemoveByIdResponse);
            Assertions.assertNotNull(secondRemoveByIdResponse);

            Assertions.assertNotNull(firstUpdateResponse);
            Assertions.assertNotNull(secondUpdateResponse);

            Assertions.assertEquals(HttpStatus.CREATED, firstCreateResponse.getStatusCode());
            Assertions.assertEquals(HttpStatus.CREATED, secondCreateResponse.getStatusCode());

            Assertions.assertEquals(HttpStatus.OK, firstGetByIdResponse.getStatusCode());
            Assertions.assertEquals(HttpStatus.OK, secondGetByIdResponse.getStatusCode());

            Assertions.assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());

            Assertions.assertEquals(HttpStatus.NO_CONTENT, firstRemoveByIdResponse.getStatusCode());
            Assertions.assertEquals(HttpStatus.NO_CONTENT, secondRemoveByIdResponse.getStatusCode());

            Assertions.assertEquals(HttpStatus.OK, firstUpdateResponse.getStatusCode());
            Assertions.assertEquals(HttpStatus.OK, secondUpdateResponse.getStatusCode());
        }
    }

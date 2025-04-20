package com.example.warehouse.dao;

import com.example.warehouse.entities.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:warehouse",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    public void setUp() {
        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setName("chair");
        testProduct.setArticle(UUID.randomUUID());
        testProduct.setCategory("furniture");
        testProduct.setDescription("chair description");
        testProduct.setPrice(new BigDecimal(10));
        testProduct.setQuantity(20);
        testProduct.setCreated(System.currentTimeMillis()/1000);
        testProduct.setLastQuantityUpdate(System.currentTimeMillis()/1000);
        testProduct = productRepository.save(testProduct);
    }

    @AfterEach
    public void tearDown() {
        productRepository.delete(testProduct);
    }

    @Test
    void createProductTest() {

        Product product = productRepository.findById(testProduct.getId()).orElse(null);

        Assertions.assertNotNull(product);
        Assertions.assertEquals(product.getId(), testProduct.getId());
        Assertions.assertEquals(product.getName(), testProduct.getName());
        Assertions.assertEquals(product.getArticle(), testProduct.getArticle());
        Assertions.assertEquals(product.getDescription(), testProduct.getDescription());
        Assertions.assertEquals(product.getCategory(), testProduct.getCategory());
        Assertions.assertEquals(product.getPrice(), testProduct.getPrice());
        Assertions.assertEquals(product.getQuantity(), testProduct.getQuantity());
        Assertions.assertEquals(product.getLastQuantityUpdate(), testProduct.getLastQuantityUpdate());
        Assertions.assertEquals(product.getCreated(), testProduct.getCreated());
    }

    @Test
    void updateProductTest() {
        testProduct.setName("table");
        testProduct.setDescription("table description");
        testProduct.setCategory("furniture");
        testProduct.setPrice(new BigDecimal(200));
        testProduct.setQuantity(30);
        testProduct.setLastQuantityUpdate((System.currentTimeMillis()+10000)/1000);
        testProduct.setCreated((System.currentTimeMillis()-10000)/1000);
        productRepository.save(testProduct);

        Product product = productRepository.findById(testProduct.getId()).orElse(null);

        Assertions.assertNotNull(product);
        Assertions.assertEquals("table", product.getName());
        Assertions.assertEquals("table description", product.getDescription());
        Assertions.assertEquals("furniture", product.getCategory());
        Assertions.assertEquals(BigDecimal.valueOf(200), product.getPrice());
        Assertions.assertEquals(30, product.getQuantity());
        Assertions.assertEquals((System.currentTimeMillis()+10000)/1000, product.getLastQuantityUpdate());
        Assertions.assertEquals((System.currentTimeMillis()-10000)/1000, product.getCreated());
    }

    @Test
    void findByArticle() {
        Product product = productRepository.findByArticle(testProduct.getArticle());

        Assertions.assertNotNull(product);
    }
}

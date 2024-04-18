package com.example.warehouse.scheduler;

import com.example.warehouse.annotation.MethodExecutionTime;
import com.example.warehouse.dao.ProductRepository;
import com.example.warehouse.entities.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Component
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class SimpleScheduler {

    private final ProductRepository productRepository;

    @MethodExecutionTime
    @Transactional
    public void updatePrice() {
        log.info("simple works");
        List<Product> products = productRepository.findAll();

        products.stream()
                .forEach(product -> product.setPrice(product.getPrice().add(product.getPrice().multiply(new BigDecimal("0.1")))));

        productRepository.saveAll(products);
    }
}

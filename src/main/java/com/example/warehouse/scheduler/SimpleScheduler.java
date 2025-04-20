package com.example.warehouse.scheduler;

import com.example.warehouse.annotation.MethodExecutionTime;
import com.example.warehouse.dao.ProductRepository;
import com.example.warehouse.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@Component
@Slf4j
@Profile("!develop")
@ConditionalOnProperty(prefix = "app.scheduling", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnExpression("#{'${app.scheduling.optimization}' == 'false'}")
public class SimpleScheduler {

    private final ProductRepository productRepository;
    private final BigDecimal value;

    public SimpleScheduler(ProductRepository productRepository,
                           @Value("${app.scheduling.value}") String value)
    {
        this.productRepository = productRepository;
        this.value = new BigDecimal(String.valueOf(value));
    }

    @MethodExecutionTime
    @Transactional
    @Scheduled(fixedDelay = 3000L)
    public void updatePrice() {
        log.info("Simple works");
        List<Product> products = productRepository.findAll();
        BigDecimal proc = BigDecimal.valueOf(1).add(value.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP));

        products.stream()
                .forEach(product -> product.setPrice(product.getPrice().multiply(proc)));

        productRepository.saveAll(products);
    }
}

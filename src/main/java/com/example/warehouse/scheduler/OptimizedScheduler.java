
package com.example.warehouse.scheduler;

import com.example.warehouse.annotation.MethodExecutionTime;
import com.example.warehouse.entities.Product;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OptimizedScheduler {

    private final JdbcTemplate jdbcTemplate;

    String filePath = "src/main/resources/scheduling-logs.txt";
    @MethodExecutionTime
    @Lock(LockModeType.WRITE)
    public void batchUpdateTest() {

        log.info("optimization works");
        List<Product> updated = jdbcTemplate.query("SELECT id, price FROM products FOR UPDATE", new BeanPropertyRowMapper<>(Product.class));
        jdbcTemplate.batchUpdate("UPDATE products SET price=price*1.1");

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath, false))){

            for (Product product : updated) {
                BigDecimal oldPrice = product.getPrice().divide(new BigDecimal("1.1"), RoundingMode.HALF_UP);
                String logMessage = String.format("Product ID: %s, Old Price: %.2f, New Price: %.2f\n", product.getId(), oldPrice, product.getPrice());
                fileWriter.write(logMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


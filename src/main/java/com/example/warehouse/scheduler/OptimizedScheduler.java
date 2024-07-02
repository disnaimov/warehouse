
package com.example.warehouse.scheduler;

import com.example.warehouse.annotation.MethodExecutionTime;
import com.example.warehouse.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@Component
@Slf4j
@Profile("!develop")
@ConditionalOnProperty(prefix = "app.scheduling", name = "enabled", havingValue = "true")
@ConditionalOnExpression("#{'${app.scheduling.optimization}' == 'true'}")
public class OptimizedScheduler {
    private static final String LOG_FILE_PATH = "src/main/resources/scheduling-logs.txt";
    private final BigDecimal value;
    private final JdbcTemplate jdbcTemplate;

    public OptimizedScheduler(@Value("${app.scheduling.value}") String value,
                              DataSource dataSource
    ) {
        this.value = new BigDecimal(String.valueOf(value));
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Scheduled(fixedDelay = 3000L)
    @MethodExecutionTime
    @Transactional
    public void batchUpdateTest() {

        log.info("Optimization works");
        List<Product> updated = jdbcTemplate.query("SELECT id, price FROM products FOR UPDATE", new BeanPropertyRowMapper<>(Product.class));
        BigDecimal oldPrice = updated.get(0).getPrice();
        BigDecimal newPrice = updated.get(0).getPrice().multiply(BigDecimal.valueOf(1).add(value.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP)));
        jdbcTemplate.update("UPDATE products SET price=price*(1 + ?/100)", value);

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(LOG_FILE_PATH, false))){

            for (Product product : updated) {
                String logMessage = String.format("Product ID: %s, Old Price: %.2f, New Price: %.2f\n", product.getId(), oldPrice, newPrice);
                fileWriter.write(logMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
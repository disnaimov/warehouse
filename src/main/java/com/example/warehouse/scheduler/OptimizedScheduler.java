
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






























    /*@Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    String filePath = "src/main/resources/scheduling-logs.txt";*/



    /*@Transactional
    @Lock(LockModeType.WRITE)
    public void batchUpdateTest() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);

        log.info("optimization works");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Product> updated = jdbcTemplate.query("SELECT id, price FROM products FOR UPDATE", new BeanPropertyRowMapper<>(Product.class));
        jdbcTemplate.update("UPDATE products SET price=price*1.1");

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

























   @Scheduled(fixedDelay = 3000L)
    public void priceUpdate() {
        log.info("Optimised works");

        final Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);

        try (session) {
            session.doWork(new Work() {
                @Override
                public void execute(final Connection connection) throws SQLException {
                    try (
                            final BufferedWriter fileWriter = new BufferedWriter(new FileWriter(LOG_FILE_PATH, false));
                            connection
                    ) {
                        connection.setAutoCommit(false);
                        final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRICE_QUERY);
                        preparedStatement.setBigDecimal(1, value);

                        final ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            fileWriter.write(buildString(resultSet));
                        }

                        connection.commit();
                    } catch (Exception e) {
                        connection.rollback();
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }


    private String buildString(final ResultSet resultSet) throws SQLException {
        final StringJoiner joiner = new StringJoiner(",");
        joiner.add(resultSet.getString("id"));
        joiner.add(resultSet.getString("name"));
        joiner.add(resultSet.getString("description"));
        joiner.add(resultSet.getString("price"));
        joiner.add(resultSet.getString("article"));

        return joiner.toString();
    }
*/

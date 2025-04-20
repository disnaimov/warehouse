package com.example.warehouse.scheduler;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

@Component
@Slf4j
@Profile("!develop")
@ConditionalOnProperty(prefix = "app.scheduling", name = "enabled", havingValue = "true")
@ConditionalOnExpression("#{'${app.scheduling.optimization}' == 'super'}")
public class SuperScheduler {

    private static final String LOG_FILE_PATH = "src/main/resources/scheduling-logs.txt";

    private static final String UPDATE_PRICE_QUERY = "UPDATE products SET price = price * (1 + ?/100) RETURNING *";

    private final EntityManagerFactory entityManagerFactory;
    private final BigDecimal value;

    public SuperScheduler(@Value("${app.scheduling.value}") String value, EntityManagerFactory entityManagerFactory) {
        this.value = new BigDecimal(String.valueOf(value));
        this.entityManagerFactory = entityManagerFactory;
    }

    @Scheduled(fixedDelay = 3000L)
    public void priceUpdate() {
        log.info("Super works");
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
}

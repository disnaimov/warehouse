CREATE TABLE IF NOT EXISTS "order" (
                                       id UUID PRIMARY KEY,
                                       customer_id BIGINT NOT NULL,
                                       status VARCHAR(255) NOT NULL,
                                       delivery_address VARCHAR(255) NOT NULL,
                                       FOREIGN KEY (customer_id) REFERENCES customer (id)
    );
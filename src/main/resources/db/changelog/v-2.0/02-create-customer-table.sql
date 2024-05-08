CREATE SEQUENCE IF NOT EXISTS customer_seq START 1;

CREATE TABLE IF NOT EXISTS customer (
                                        id            BIGINT                    DEFAULT nextval('customer_seq') not null,
    login         VARCHAR(255)  UNIQUE      not null,
    email         VARCHAR(255)              not null,
    is_active     BOOLEAN                   not null,
    PRIMARY KEY (id)
    );
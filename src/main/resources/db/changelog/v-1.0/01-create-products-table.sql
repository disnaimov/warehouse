CREATE TABLE IF NOT EXISTS products (
                                        id            uuid            not null,
                                        name          TEXT    not null,
                                        article       uuid     UNIQUE     not null,
                                        description   TEXT            not null,
                                        category      TEXT    not null,
                                        price         NUMERIC         not null,
                                        quantity      INT             not null,
                                        last_quantity_update          BIGINT   not null,
                                        creation_date                 BIGINT       not null,
                                        UNIQUE      (id),
    PRIMARY KEY (id)
    );
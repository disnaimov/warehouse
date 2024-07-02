CREATE TABLE IF NOT EXISTS products (
                                        id            uuid            not null,
                                        name          TEXT    not null,
                                        article       uuid     UNIQUE     not null,
                                        description   TEXT            not null,
                                        category      TEXT    not null,
                                        price         NUMERIC(10, 2)         not null,
                                        quantity      INT             not null,
                                        last_quantity_update          TIMESTAMP   not null,
                                        creation_date                 DATE   not null,
                                        UNIQUE      (id),
    PRIMARY KEY (id)
    );
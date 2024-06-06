CREATE TABLE IF NOT EXISTS product_info (
                                            productFileId            uuid    not null,
                                        product_name          TEXT    not null,
    UNIQUE      (productFileId),
    PRIMARY KEY (productFileId)
    );
CREATE TABLE IF NOT EXISTS products (
    id            uuid            not null,
    name          TEXT    not null,
    article       TEXT     UNIQUE     not null,
    description   TEXT            not null,
    category      TEXT    not null,
    price         DECIMAL         not null,
    quantity      INT                     ,
    last_quantity_update          TIMESTAMP   not null,
    creation_date                 TIMESTAMP   not null,
     UNIQUE      (id),
    PRIMARY KEY (id)
    );
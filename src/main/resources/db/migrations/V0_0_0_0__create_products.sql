CREATE TABLE IF NOT EXISTS products (
    id            uuid            not null,
    name          varchar(255)    not null,
    article       varchar(255)     UNIQUE     not null,
    description   TEXT            not null,
    category      varchar(255)    not null,
    price         DECIMAL         not null,
    quantity      INT                     ,
    last_quantity_update          TIMESTAMP   not null,
    creation_date                 TIMESTAMP   not null,
     UNIQUE      (id),
    PRIMARY KEY (id)
    );
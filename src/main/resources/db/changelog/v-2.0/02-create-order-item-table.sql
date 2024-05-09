CREATE TABLE IF NOT EXISTS order_item (
                                          order_id UUID NOT NULL,
                                          product_id UUID NOT NULL,
                                          price NUMERIC NOT NULL,
                                          quantity INT NOT NULL,
                                          PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES "order" (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
    );
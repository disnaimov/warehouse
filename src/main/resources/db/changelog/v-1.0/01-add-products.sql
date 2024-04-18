INSERT INTO products(
                            id, name, article, description,
                            category, price, quantity,
                            last_quantity_update, creation_date
)
SELECT gen_random_uuid(), 'chair', gen_random_uuid(), 'chair description',
       'furniture', '10', '1', '2003-03-25 05:30:30', '2003-03-25'
FROM generate_series(1, 1000000)
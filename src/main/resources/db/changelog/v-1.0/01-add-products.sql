INSERT INTO products(
                            id, name, article, description,
                            category, price, quantity,
                            last_quantity_update, creation_date
)
SELECT gen_random_uuid(), 'chair', gen_random_uuid(), 'chair description',
       'furniture', '10', '1', '1713855053', '1713855533'
FROM generate_series(1, 100)
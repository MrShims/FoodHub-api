ALTER TABLE orders ALTER COLUMN user_id TYPE VARCHAR(255);
ALTER TABLE orders_line_dishes ADD COLUMN price DECIMAL(10, 2);
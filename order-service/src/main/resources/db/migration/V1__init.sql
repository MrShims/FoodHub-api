CREATE TABLE orders
(
    id               BIGSERIAL  PRIMARY KEY,
    user_id          bigint,
    status           VARCHAR(255),
    creation_date    TIMESTAMP,
    update_date      TIMESTAMP,
    delivery_address VARCHAR(255),
    order_amount     DECIMAL(10, 2),
    payment_method   VARCHAR(255),
    contact          VARCHAR(255)
);
CREATE TABLE orders_line_dishes
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255),
    quantity INT,
    order_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES orders(id)

);



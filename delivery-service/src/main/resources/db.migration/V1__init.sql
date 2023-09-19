CREATE TABLE delivery
(
    id BIGSERIAL  PRIMARY KEY,
    order_id bigint,
    courier_id varchar(100),
    delivery_address varchar(100),
    isDeliver boolean

);
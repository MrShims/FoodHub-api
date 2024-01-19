CREATE TABLE tbl_order_status (
                                  id BIGSERIAL PRIMARY KEY,
                                  statusName VARCHAR(255) NOT NULL
);

ALTER TABLE orders ADD order_status_id BIGINT;
ALTER TABLE orders ADD CONSTRAINT fk_order_status FOREIGN KEY (order_status_id) REFERENCES tbl_order_status (id);

CREATE SEQUENCE order_status_seq START WITH 1 INCREMENT BY 1;
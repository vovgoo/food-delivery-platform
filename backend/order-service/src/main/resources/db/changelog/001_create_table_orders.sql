CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    total_price NUMERIC(10,2) NOT NULL,
    CONSTRAINT chk_orders_status CHECK (status IN (
        'CREATED','CONFIRMED','PREPARING','READY','DELIVERING','COMPLETED','CANCELLED'
    )),
    CONSTRAINT chk_orders_total_price CHECK (total_price >= 0)
);

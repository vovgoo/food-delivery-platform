CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    dish_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity >= 1),
    price NUMERIC(10,2) NOT NULL CHECK (price > 0),
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

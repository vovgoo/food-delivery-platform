CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status order_status NOT NULL,
    order_date TIMESTAMP NOT NULL DEFAULT now(),
    user_id UUID NOT NULL,
    delivery_address UUID NOT NULL,
    restaurant_id UUID NOT NULL,
    total_price NUMERIC(10,2) NOT NULL
);

CREATE TYPE order_status AS ENUM (
    'CREATED',
    'CONFIRMED',
    'PREPARING',
    'READY',
    'DELIVERING',
    'COMPLETED',
    'CANCELLED'
    );

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status order_status NOT NULL,
    order_date TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id UUID NOT NULL,
    delivery_address UUID NOT NULL,
    restaurant_id UUID NOT NULL,
    total_price NUMERIC(10,2) NOT NULL CHECK (total_price >= 0),
    payment_id UUID UNIQUE
);

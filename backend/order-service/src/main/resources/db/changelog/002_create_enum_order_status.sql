CREATE TYPE order_status AS ENUM (
    'CREATED',
    'CONFIRMED',
    'PREPARING',
    'READY',
    'DELIVERING',
    'COMPLETED',
    'CANCELLED'
);

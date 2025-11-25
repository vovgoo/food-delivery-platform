CREATE TABLE restaurants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    cuisine VARCHAR(50) NOT NULL,
    address VARCHAR(200) NOT NULL,
    website VARCHAR(200),
    phone VARCHAR(50) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    delivery_available BOOLEAN NOT NULL,
    parking_available BOOLEAN NOT NULL,
    status restaurant_status NOT NULL DEFAULT 'ACTIVE'
);

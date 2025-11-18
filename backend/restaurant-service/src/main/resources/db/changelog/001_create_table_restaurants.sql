CREATE TABLE restaurants (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    cuisine VARCHAR(50) NOT NULL,
    address VARCHAR(200) NOT NULL,
    website VARCHAR(200),
    profile_image_id UUID,
    phone VARCHAR(20) UNIQUE NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    delivery_available BOOLEAN NOT NULL,
    parking_available BOOLEAN NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT chk_restaurant_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'CLOSED'))
);

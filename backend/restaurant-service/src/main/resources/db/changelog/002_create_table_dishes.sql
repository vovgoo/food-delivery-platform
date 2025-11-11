CREATE TABLE dishes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price NUMERIC(8,2) NOT NULL,
    image_url VARCHAR(500),
    restaurant_id BIGINT NOT NULL
);

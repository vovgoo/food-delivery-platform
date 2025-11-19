CREATE TABLE restaurant_images (
    id UUID PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    is_profile BOOLEAN NOT NULL DEFAULT FALSE,
    restaurant_id UUID NOT NULL
);

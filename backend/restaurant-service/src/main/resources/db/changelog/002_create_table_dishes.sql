CREATE TABLE dishes (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    profile_image_id UUID,
    portion_in_grams INTEGER,
    proteins DOUBLE PRECISION,
    fats DOUBLE PRECISION,
    carbohydrates DOUBLE PRECISION,
    spicy BOOLEAN NOT NULL DEFAULT FALSE,
    vegan BOOLEAN NOT NULL DEFAULT FALSE,
    vegetarian BOOLEAN NOT NULL DEFAULT FALSE,
    price NUMERIC(8,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    restaurant_id UUID NOT NULL,
    CONSTRAINT chk_dish_status CHECK (status IN ('AVAILABLE', 'TEMPORARY_UNAVAILABLE', 'REMOVED'))
);

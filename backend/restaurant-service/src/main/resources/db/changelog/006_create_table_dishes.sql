CREATE TABLE dishes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    portion_in_grams INTEGER CHECK (portion_in_grams >= 0),
    proteins DOUBLE PRECISION CHECK (proteins >= 0),
    fats DOUBLE PRECISION CHECK (fats >= 0),
    carbohydrates DOUBLE PRECISION CHECK (carbohydrates >= 0),
    spicy BOOLEAN NOT NULL DEFAULT FALSE,
    vegan BOOLEAN NOT NULL DEFAULT FALSE,
    vegetarian BOOLEAN NOT NULL DEFAULT FALSE,
    price NUMERIC(8,2) NOT NULL CHECK (price > 0),
    status dish_status NOT NULL DEFAULT 'AVAILABLE',
    restaurant_id UUID NOT NULL,
    CONSTRAINT fk_dish_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

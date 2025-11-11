ALTER TABLE dishes
    ADD CONSTRAINT fk_dish_restaurant
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
    ON DELETE CASCADE;

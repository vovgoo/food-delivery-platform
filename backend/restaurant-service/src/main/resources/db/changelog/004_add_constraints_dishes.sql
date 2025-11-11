ALTER TABLE dishes
    ADD CONSTRAINT chk_dish_name_length CHECK (char_length(name) BETWEEN 2 AND 100),
    ADD CONSTRAINT chk_dish_description_length CHECK (char_length(description) <= 500),
    ADD CONSTRAINT chk_dish_price_positive CHECK (price >= 0.01);

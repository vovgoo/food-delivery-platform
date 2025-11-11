ALTER TABLE restaurants
    ADD CONSTRAINT chk_restaurant_name_length CHECK (char_length(name) BETWEEN 2 AND 100),
    ADD CONSTRAINT chk_restaurant_cuisine_length CHECK (char_length(cuisine) BETWEEN 2 AND 50),
    ADD CONSTRAINT chk_restaurant_address_length CHECK (char_length(address) BETWEEN 5 AND 200);

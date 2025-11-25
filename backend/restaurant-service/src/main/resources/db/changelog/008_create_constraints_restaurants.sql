ALTER TABLE restaurants
    ADD CONSTRAINT chk_restaurants_name CHECK (char_length(name) BETWEEN 2 AND 100),
    ADD CONSTRAINT chk_restaurants_cuisine CHECK (char_length(cuisine) BETWEEN 2 AND 50),
    ADD CONSTRAINT chk_restaurants_address CHECK (char_length(address) BETWEEN 5 AND 200),
    ADD CONSTRAINT chk_restaurants_website CHECK (char_length(website) <= 200);
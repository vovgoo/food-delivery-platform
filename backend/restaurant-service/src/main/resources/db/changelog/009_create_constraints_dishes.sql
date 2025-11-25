ALTER TABLE dishes
    ADD CONSTRAINT chk_dishes_name CHECK (char_length(name) BETWEEN 2 AND 100),
    ADD CONSTRAINT chk_dishes_description CHECK (char_length(description) <= 500),
    ADD CONSTRAINT chk_dishes_price CHECK (price > 0);
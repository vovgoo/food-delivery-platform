ALTER TABLE addresses
    ADD CONSTRAINT chk_street_length CHECK (LENGTH(street) <= 255),
    ADD CONSTRAINT chk_city_length CHECK (LENGTH(city) <= 100),
    ADD CONSTRAINT chk_zip_length CHECK (LENGTH(zip) <= 20),
    ADD CONSTRAINT chk_state_length CHECK (LENGTH(state) <= 100),
    ADD CONSTRAINT chk_country_length CHECK (LENGTH(country) <= 100);

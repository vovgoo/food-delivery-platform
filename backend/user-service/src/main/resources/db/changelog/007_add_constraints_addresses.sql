ALTER TABLE addresses
    ADD CONSTRAINT chk_country_length CHECK (LENGTH(country) <= 100),
    ADD CONSTRAINT chk_state_length CHECK (LENGTH(state) <= 100),
    ADD CONSTRAINT chk_city_length CHECK (LENGTH(city) <= 100),
    ADD CONSTRAINT chk_street_length CHECK (LENGTH(street) <= 255),
    ADD CONSTRAINT chk_house_length CHECK (LENGTH(house) <= 20),
    ADD CONSTRAINT chk_building_length CHECK (LENGTH(building) <= 10),
    ADD CONSTRAINT chk_apartment_length CHECK (LENGTH(apartment) <= 10),
    ADD CONSTRAINT chk_delivery_instructions_length CHECK (LENGTH(delivery_instructions) <= 500),
    ADD CONSTRAINT chk_zip_length CHECK (LENGTH(zip) <= 20),
    ADD CONSTRAINT chk_address_status CHECK (address_status IN ('ACTIVE', 'DELETED'));
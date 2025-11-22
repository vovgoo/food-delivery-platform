ALTER TABLE addresses
    ADD CONSTRAINT chk_addresses_country_length CHECK (char_length(country) <= 100),
    ADD CONSTRAINT chk_addresses_state_length CHECK (char_length(state) <= 100),
    ADD CONSTRAINT chk_addresses_city_length CHECK (char_length(city) <= 100),
    ADD CONSTRAINT chk_addresses_street_length CHECK (char_length(street) <= 255),
    ADD CONSTRAINT chk_addresses_house_length CHECK (char_length(house) <= 20),
    ADD CONSTRAINT chk_addresses_building_length CHECK (char_length(building) <= 10),
    ADD CONSTRAINT chk_addresses_apartment_length CHECK (char_length(apartment) <= 10),
    ADD CONSTRAINT chk_addresses_zip_length CHECK (char_length(zip) <= 20),
    ADD CONSTRAINT chk_addresses_delivery_instructions_length CHECK (char_length(delivery_instructions) <= 500);

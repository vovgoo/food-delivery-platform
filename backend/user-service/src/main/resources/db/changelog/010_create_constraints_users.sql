ALTER TABLE users
    ADD CONSTRAINT chk_users_full_name CHECK (char_length(full_name) BETWEEN 2 AND 100),
    ADD CONSTRAINT chk_users_birth_past CHECK (birth_date < CURRENT_DATE);
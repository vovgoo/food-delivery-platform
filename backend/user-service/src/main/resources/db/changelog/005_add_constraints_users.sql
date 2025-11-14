ALTER TABLE users
    ADD CONSTRAINT chk_email_length CHECK (LENGTH(email) <= 255),
    ADD CONSTRAINT chk_full_name_length CHECK (LENGTH(full_name) BETWEEN 2 AND 100),
    ADD CONSTRAINT chk_phone_length CHECK (LENGTH(phone) <= 20),
    ADD CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'DEACTIVATED', 'BLOCKED')),
    ADD CONSTRAINT chk_birth_date CHECK (birth_date < CURRENT_DATE);
ALTER TABLE roles
    ADD CONSTRAINT chk_role_name_length CHECK (LENGTH(name) <= 50);

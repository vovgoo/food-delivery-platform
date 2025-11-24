CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name role_type_enum NOT NULL UNIQUE
);

INSERT INTO roles (id, name) VALUES (gen_random_uuid(), 'ADMIN');
INSERT INTO roles (id, name) VALUES (gen_random_uuid(), 'USER');
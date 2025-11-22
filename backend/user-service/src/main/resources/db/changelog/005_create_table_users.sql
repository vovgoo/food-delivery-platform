CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    email VARCHAR(255) UNIQUE,
    phone VARCHAR(50) UNIQUE NOT NULL,

    full_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,

    status user_status_enum NOT NULL DEFAULT 'ACTIVE',
    password_hash VARCHAR(255) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

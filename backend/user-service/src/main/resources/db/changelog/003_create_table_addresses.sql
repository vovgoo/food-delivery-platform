CREATE TABLE addresses (
   id BIGSERIAL PRIMARY KEY,
   country VARCHAR(100) NOT NULL,
   state VARCHAR(100) NOT NULL,
   city VARCHAR(100) NOT NULL,
   street VARCHAR(255) NOT NULL,
   house VARCHAR(20) NOT NULL,
   building VARCHAR(10),
   apartment VARCHAR(10),
   delivery_instructions VARCHAR(500),
   zip VARCHAR(20) NOT NULL,
   is_default BOOLEAN NOT NULL DEFAULT FALSE,
   address_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
   user_id BIGINT NOT NULL
);

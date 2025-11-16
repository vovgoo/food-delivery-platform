CREATE TABLE email_domains (
    id BIGSERIAL PRIMARY KEY,
    domain VARCHAR(255) NOT NULL UNIQUE,
    allowed BOOLEAN NOT NULL DEFAULT TRUE
);

ALTER TABLE email_domains
    ADD CONSTRAINT chk_domain_length CHECK (LENGTH(domain) <= 255);
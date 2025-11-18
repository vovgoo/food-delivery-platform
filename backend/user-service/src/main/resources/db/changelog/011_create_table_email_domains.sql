CREATE TABLE email_domains (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    domain VARCHAR(255) NOT NULL UNIQUE,
    allowed BOOLEAN NOT NULL DEFAULT TRUE
);

ALTER TABLE email_domains
    ADD CONSTRAINT chk_domain_length CHECK (LENGTH(domain) <= 255);
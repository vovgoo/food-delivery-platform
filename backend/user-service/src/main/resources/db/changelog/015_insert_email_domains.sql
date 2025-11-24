INSERT INTO email_domains (domain, allowed) VALUES
    ('gmail.com', TRUE),
    ('yahoo.com', TRUE),
    ('outlook.com', TRUE),
    ('hotmail.com', TRUE),
    ('icloud.com', TRUE),
    ('aol.com', TRUE),
    ('protonmail.com', TRUE),
    ('zoho.com', TRUE),
    ('innowise.com', TRUE),
    ('example.com', TRUE)
ON CONFLICT (domain) DO NOTHING;

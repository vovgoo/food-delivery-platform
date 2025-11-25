ALTER TABLE images
    ADD CONSTRAINT chk_images_url CHECK (char_length(url) <= 255);
CREATE INDEX idx_images_type_parent ON images(type, parent_id);
CREATE INDEX idx_images_is_profile ON images(is_profile);
CREATE UNIQUE INDEX unique_profile_image_per_object ON images(type, parent_id) WHERE is_profile = TRUE;
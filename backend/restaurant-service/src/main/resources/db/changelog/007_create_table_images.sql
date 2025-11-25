CREATE TABLE images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    url VARCHAR(255) NOT NULL,
    is_profile BOOLEAN NOT NULL DEFAULT FALSE,
    type image_type NOT NULL,
    parent_id UUID NOT NULL
);

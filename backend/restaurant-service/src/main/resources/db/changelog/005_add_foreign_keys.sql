ALTER TABLE restaurants
    ADD CONSTRAINT fk_restaurant_profile_image
        FOREIGN KEY (profile_image_id) REFERENCES restaurant_images(id);

ALTER TABLE restaurant_images
    ADD CONSTRAINT fk_restaurant_image_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE;

ALTER TABLE dishes
    ADD CONSTRAINT fk_dish_profile_image
        FOREIGN KEY (profile_image_id) REFERENCES dish_images(id);

ALTER TABLE dish_images
    ADD CONSTRAINT fk_dish_image_dish
        FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE;

ALTER TABLE dishes
    ADD CONSTRAINT fk_dish_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE;

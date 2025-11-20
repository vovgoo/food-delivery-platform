CREATE INDEX idx_restaurants_name ON restaurants(name);
CREATE INDEX idx_dishes_name ON dishes(name);

CREATE INDEX idx_dishes_restaurant_id ON dishes(restaurant_id);
CREATE INDEX idx_restaurant_images_restaurant_id ON restaurant_images(restaurant_id);
CREATE INDEX idx_dish_images_dish_id ON dish_images(dish_id);
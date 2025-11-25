package org.vovgoo.restaurantservice.service.image;

import org.vovgoo.restaurantservice.dto.image.internal.ImageRequest;
import org.vovgoo.restaurantservice.entity.Image;

public interface ImageService {
    Image upload(ImageRequest request);
    void remove(ImageRequest request);
}

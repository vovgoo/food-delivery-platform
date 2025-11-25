package org.vovgoo.restaurantservice.exception.custom.image;

public class ImageHandlerNotFoundException extends RuntimeException {
    public ImageHandlerNotFoundException(Class<?> type) {
        super("No image handler found for entity: " + type.getSimpleName());
    }
}

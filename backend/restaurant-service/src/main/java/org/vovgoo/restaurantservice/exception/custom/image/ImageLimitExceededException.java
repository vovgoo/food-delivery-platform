package org.vovgoo.restaurantservice.exception.custom.image;

public class ImageLimitExceededException extends RuntimeException {
    public ImageLimitExceededException(Long limit) {
        super("Нельзя добавить больше " + limit + " изображений");
    }
}

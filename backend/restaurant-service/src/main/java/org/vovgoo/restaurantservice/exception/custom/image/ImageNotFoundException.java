package org.vovgoo.restaurantservice.exception.custom.image;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException() {
      super("Изображение не найдено");
    }
}

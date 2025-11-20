package org.vovgoo.restaurantservice.exception.custom.image;

public class ImageReadException extends RuntimeException {
    public ImageReadException() {
        super("Ошибка при чтении файла изображения");
    }
}

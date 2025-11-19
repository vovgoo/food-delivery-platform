package org.vovgoo.restaurantservice.exception.custom.image;

public class ImageUploadException extends RuntimeException {
  public ImageUploadException() {
    super("Ошибка при загрузке изображения на сервер ImgBB");
  }
}

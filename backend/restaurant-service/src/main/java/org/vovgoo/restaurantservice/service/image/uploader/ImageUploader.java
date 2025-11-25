package org.vovgoo.restaurantservice.service.image.uploader;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
    String upload(MultipartFile file);
}

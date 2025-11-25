package org.vovgoo.restaurantservice.service.image.service;

import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.enums.ImageType;

import java.util.UUID;

public interface ImageService {
    void uploadImage(UUID parentId, ImageType type, MultipartFile file, Long limit);
    void removeImage(UUID parentId, UUID imageId, ImageType type);
    void uploadProfileImage(UUID parentId, ImageType type, MultipartFile file);
    void removeProfileImage(UUID parentId, ImageType type);
}

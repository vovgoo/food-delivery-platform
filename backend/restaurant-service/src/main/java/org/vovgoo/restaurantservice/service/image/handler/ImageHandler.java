package org.vovgoo.restaurantservice.service.image.handler;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageHandler <T> {
    void uploadImage(T entity, MultipartFile file);
    void removeImage(T entity, UUID imageId);
    void uploadProfileImage(T entity, MultipartFile file);
    void removeProfileImage(T entity);
    Class<T> getEntityClass();
}

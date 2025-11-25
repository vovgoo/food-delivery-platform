package org.vovgoo.restaurantservice.service.image.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.service.image.handler.resolver.ImageHandlerResolver;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageFacadeService {

    private final ImageHandlerResolver handlerResolver;

    public <T> void uploadImage(T entity, MultipartFile file) {
        handlerResolver.getHandlerFor(entity).uploadImage(entity, file);
    }

    public <T> void removeImage(T entity, UUID imageId) {
        handlerResolver.getHandlerFor(entity).removeImage(entity, imageId);
    }

    public <T> void uploadProfileImage(T entity, MultipartFile file) {
        handlerResolver.getHandlerFor(entity).uploadProfileImage(entity, file);
    }

    public <T> void removeProfileImage(T entity) {
        handlerResolver.getHandlerFor(entity).removeProfileImage(entity);
    }
}


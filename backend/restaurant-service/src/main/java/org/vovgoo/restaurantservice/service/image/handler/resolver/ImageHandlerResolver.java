package org.vovgoo.restaurantservice.service.image.handler.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.restaurantservice.exception.custom.image.ImageHandlerNotFoundException;
import org.vovgoo.restaurantservice.service.image.handler.ImageHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageHandlerResolver {

    private final List<ImageHandler<?>> handlers;

    @SuppressWarnings("unchecked")
    public <T> ImageHandler<T> getHandlerFor(T entity) {

        return (ImageHandler<T>) handlers.stream()
                .filter(h -> h.getEntityClass().isAssignableFrom(entity.getClass()))
                .findFirst()
                .orElseThrow(
                        () -> new ImageHandlerNotFoundException(entity.getClass())
                );
    }
}

package org.vovgoo.restaurantservice.service.image.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.restaurantservice.dto.image.internal.ImageRequest;
import org.vovgoo.restaurantservice.entity.Image;
import org.vovgoo.restaurantservice.exception.custom.image.ImageNotFoundException;
import org.vovgoo.restaurantservice.repository.ImageRepository;
import org.vovgoo.restaurantservice.service.image.ImageService;
import org.vovgoo.restaurantservice.service.image.ImageUploader;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageUploader imageUploader;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public Image upload(ImageRequest request) {
        String url = imageUploader.upload(request.file());

        if (request.isProfile()) {
            imageRepository.findByParentIdAndTypeAndIsProfile(request.parentId(), request.type(), true)
                    .ifPresent(imageRepository::delete);
        }

        Image image = Image.builder()
                .url(url)
                .isProfile(request.isProfile())
                .parentId(request.parentId())
                .type(request.type())
                .build();

        return imageRepository.save(image);
    }

    @Override
    @Transactional
    public void remove(ImageRequest request) {
        Image image = imageRepository.findByParentIdAndTypeAndIsProfile(request.parentId(), request.type(), request.isProfile())
                .orElseThrow(ImageNotFoundException::new);

        imageRepository.delete(image);
    }
}
package org.vovgoo.restaurantservice.service.image.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.Image;
import org.vovgoo.restaurantservice.entity.enums.ImageType;
import org.vovgoo.restaurantservice.exception.custom.image.ImageNotFoundException;
import org.vovgoo.restaurantservice.repository.ImageRepository;
import org.vovgoo.restaurantservice.service.image.ImageService;
import org.vovgoo.restaurantservice.service.image.ImageUploader;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageUploader imageUploader;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public void uploadImage(UUID parentId, ImageType type, MultipartFile file) {
        String url = imageUploader.upload(file);

        Image image = Image.builder()
                .url(url)
                .isProfile(false)
                .parentId(parentId)
                .type(type)
                .build();

        imageRepository.save(image);
    }

    @Override
    @Transactional
    public void removeImage(UUID parentId, UUID imageId, ImageType type) {
        Image image = imageRepository.findByIdAndParentIdAndType(imageId, parentId, type)
                .orElseThrow(ImageNotFoundException::new);

        imageRepository.delete(image);
    }

    @Override
    @Transactional
    public void uploadProfileImage(UUID parentId, ImageType type, MultipartFile file) {
        String url = imageUploader.upload(file);

        imageRepository.findProfileImage(parentId, type)
                .ifPresent(imageRepository::delete);

        Image image = Image.builder()
                .url(url)
                .isProfile(true)
                .parentId(parentId)
                .type(type)
                .build();

        imageRepository.save(image);
    }

    @Override
    @Transactional
    public void removeProfileImage(UUID parentId, ImageType type) {
        Image image = imageRepository.findProfileImage(parentId, type)
                .orElseThrow(ImageNotFoundException::new);

        imageRepository.delete(image);
    }
}
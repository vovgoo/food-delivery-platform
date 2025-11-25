package org.vovgoo.restaurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.restaurantservice.entity.Image;
import org.vovgoo.restaurantservice.entity.enums.ImageType;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    Optional<Image> findByParentIdAndTypeAndIsProfile(UUID parentId, ImageType type, Boolean isProfile);
}

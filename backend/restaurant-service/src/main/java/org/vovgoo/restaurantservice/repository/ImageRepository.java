package org.vovgoo.restaurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.common.domain.image.enums.ImageType;
import org.vovgoo.restaurantservice.entity.Image;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {

    @Query("SELECT i FROM Image i WHERE i.parentId = :parentId AND i.type = :type AND i.isProfile = true")
    Optional<Image> findProfileImage(@Param("parentId") UUID parentId, @Param("type") ImageType type);

    @Query("SELECT i FROM Image i WHERE i.parentId IN :parentIds AND i.type = :type AND i.isProfile = true")
    List<Image> findProfileImagesByParentIds(@Param("parentIds") List<UUID> parentIds, @Param("type") ImageType type);

    Optional<Image> findByIdAndParentIdAndType(UUID id, UUID parentId, ImageType type);

    List<Image> findAllByParentIdAndType(UUID parentId, ImageType type);

    @Query("SELECT COUNT(i) FROM Image i WHERE i.parentId = :parentId AND i.type = :type AND i.isProfile = false")
    Long countImages(@Param("parentId") UUID parentId, @Param("type") ImageType type);
}

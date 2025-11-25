package org.vovgoo.restaurantservice.dto.image.internal;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.enums.ImageType;

import java.util.UUID;

@Builder
public record ImageRequest(
        UUID parentId,
        ImageType type,
        MultipartFile file,
        boolean isProfile
) {}

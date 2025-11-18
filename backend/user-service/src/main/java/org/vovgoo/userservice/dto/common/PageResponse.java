package org.vovgoo.userservice.dto.common;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "Generic paginated response")
public class PageResponse<T> {

    @ArraySchema(schema = @Schema(description = "List of items on the current page"))
    private final List<T> content;

    @Schema(description = "Current page number (zero-based)", example = "0")
    private final int pageNumber;

    @Schema(description = "Number of items per page", example = "20")
    private final int pageSize;

    @Schema(description = "Total number of elements across all pages", example = "150")
    private final long totalElements;

    @Schema(description = "Total number of pages available", example = "8")
    private final int totalPages;

    public static <T> PageResponse<T> of(Page<T> page) {
        if (page == null) {
            return new PageResponse<>(List.of(), 0, 0, 0, 0);
        }

        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}

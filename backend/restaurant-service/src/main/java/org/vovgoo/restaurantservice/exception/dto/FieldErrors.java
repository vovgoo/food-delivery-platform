package org.vovgoo.restaurantservice.exception.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class FieldErrors {

    private final List<FieldErrorDetail> errors;

    public FieldErrors(List<FieldError> fieldErrors) {
        this.errors = fieldErrors.stream()
                .map(err -> FieldErrorDetail.builder()
                        .field(err.getField())
                        .messages(List.of(Objects.requireNonNull(err.getDefaultMessage())))
                        .build())
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class FieldErrorDetail {
        private final String field;
        private final List<String> messages;
    }
}

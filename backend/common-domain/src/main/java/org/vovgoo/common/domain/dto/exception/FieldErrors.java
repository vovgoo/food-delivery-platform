package org.vovgoo.common.domain.dto.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FieldErrors {

    private final List<FieldErrorDetail> errors;

    public static FieldErrors of(List<FieldErrorDetail> errors) {
        return new FieldErrors(errors);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class FieldErrorDetail {
        private final String field;
        private final List<String> messages;
    }
}

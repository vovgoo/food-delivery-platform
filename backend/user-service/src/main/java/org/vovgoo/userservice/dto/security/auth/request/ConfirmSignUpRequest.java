package org.vovgoo.userservice.dto.security.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ConfirmSignUpRequest(

        @NotBlank(message = "Код не может быть пустым")
        @Pattern(regexp = "\\d{6}", message = "Код должен состоять ровно из 6 цифр")
        String code
) {}

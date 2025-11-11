package org.vovgoo.userservice.dto.user.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.vovgoo.userservice.dto.address.request.AddressUpsertRequest;

import java.util.List;

public record UserUpdateRequest(
        @Email(message = "Email должен быть корректным")
        @NotBlank(message = "Email не может быть пустым")
        @Size(max = 255, message = "Email слишком длинный")
        String email,

        @NotBlank(message = "Полное имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
        String fullName,

        @Valid
        List<AddressUpsertRequest> addresses
) {}

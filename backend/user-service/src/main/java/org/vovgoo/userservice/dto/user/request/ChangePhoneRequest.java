package org.vovgoo.userservice.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import org.vovgoo.userservice.validators.phone.Phone;

public record ChangePhoneRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        String phone
) {}

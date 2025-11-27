package org.vovgoo.orderservice.dto.payment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.common.domain.payment.enums.PaymentMethod;
import org.vovgoo.common.domain.payment.enums.PaymentStatus;

import java.util.UUID;

@Schema(description = "Response containing details of a payment")
public record PaymentResponse(

        @Schema(description = "Unique payment ID", example = "990e8400-e29b-41d4-a716-446655440444")
        UUID id,

        @Schema(description = "Payment method used for the order", example = "CREDIT_CARD")
        PaymentMethod method,

        @Schema(description = "Current status of the payment", example = "PAID")
        PaymentStatus status
) {}

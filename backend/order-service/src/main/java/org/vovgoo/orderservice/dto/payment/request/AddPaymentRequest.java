package org.vovgoo.orderservice.dto.payment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.vovgoo.orderservice.entity.enums.PaymentMethod;

@Schema(description = "Request to add payment details to an order")
public record AddPaymentRequest(

        @Schema(description = "Payment method for the order", example = "CREDIT_CARD")
        @NotNull(message = "Необходимо указать способ оплаты")
        PaymentMethod paymentMethod
) {}

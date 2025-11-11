package org.vovgoo.orderservice.dto.payment.request;

import jakarta.validation.constraints.NotNull;
import org.vovgoo.orderservice.entity.enums.PaymentMethod;

public record AddPaymentRequest(
        @NotNull(message = "Необходимо указать способ оплаты")
        PaymentMethod paymentMethod
) {}

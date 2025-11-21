package org.vovgoo.orderservice.dto.payment.response;

import java.util.UUID;

import org.vovgoo.orderservice.entity.enums.PaymentMethod;
import org.vovgoo.orderservice.entity.enums.PaymentStatus;

public record PaymentResponse(
        UUID id,
        PaymentMethod method,
        PaymentStatus status
) {}

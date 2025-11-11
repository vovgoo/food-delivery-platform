package org.vovgoo.orderservice.dto.payment.response;

import java.math.BigDecimal;
import org.vovgoo.orderservice.entity.enums.PaymentMethod;
import org.vovgoo.orderservice.entity.enums.PaymentStatus;

public record PaymentResponse(
        Long id,
        PaymentMethod method,
        BigDecimal amount,
        PaymentStatus status
) {}

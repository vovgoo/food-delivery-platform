package org.vovgoo.orderservice.service.payment.handlers.impl;

import org.springframework.stereotype.Component;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.Payment;
import org.vovgoo.common.domain.payment.enums.PaymentMethod;
import org.vovgoo.common.domain.payment.enums.PaymentStatus;
import org.vovgoo.orderservice.service.payment.handlers.PaymentHandler;

@Component
public class DebitCardPaymentHandler implements PaymentHandler {

    @Override
    public Payment process(Order order) {
        return Payment.builder()
                .order(order)
                .method(PaymentMethod.DEBIT_CARD)
                .status(PaymentStatus.COMPLETED)
                .amount(order.getTotalPrice())
                .build();
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.DEBIT_CARD;
    }
}
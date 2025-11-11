package org.vovgoo.orderservice.service.payment.handlers;

import org.springframework.stereotype.Component;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.Payment;
import org.vovgoo.orderservice.entity.enums.PaymentMethod;
import org.vovgoo.orderservice.entity.enums.PaymentStatus;

@Component
public class PaypalPaymentHandler implements PaymentHandler {

    @Override
    public Payment process(Order order) {
        return Payment.builder()
                .order(order)
                .method(PaymentMethod.PAYPAL)
                .status(PaymentStatus.COMPLETED)
                .amount(order.getTotalPrice())
                .build();
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.PAYPAL;
    }
}

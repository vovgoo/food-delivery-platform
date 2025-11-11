package org.vovgoo.orderservice.service.payment.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vovgoo.orderservice.entity.enums.PaymentMethod;
import org.vovgoo.orderservice.service.payment.handlers.PaymentHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    private final List<PaymentHandler> handlers;

    public PaymentHandler getHandler(PaymentMethod method) {
        return handlers.stream()
                .filter(h -> h.getPaymentMethod() == method)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не поддерживаемый метод оплаты: " + method));
    }
}
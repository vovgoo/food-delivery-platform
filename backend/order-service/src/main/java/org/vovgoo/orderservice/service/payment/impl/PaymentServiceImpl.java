package org.vovgoo.orderservice.service.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.orderservice.dto.payment.request.AddPaymentRequest;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.Payment;
import org.vovgoo.orderservice.repository.PaymentRepository;
import org.vovgoo.orderservice.service.payment.PaymentService;
import org.vovgoo.orderservice.service.payment.factory.PaymentFactory;
import org.vovgoo.orderservice.service.payment.handlers.PaymentHandler;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentFactory paymentFactory;
    private final PaymentRepository paymentRepository;

    @Override
    public Payment processPayment(Order order, AddPaymentRequest addPaymentRequest) {
        PaymentHandler handler = paymentFactory.getHandler(addPaymentRequest.paymentMethod());
        Payment payment = handler.process(order);
        return paymentRepository.save(payment);
    }
}

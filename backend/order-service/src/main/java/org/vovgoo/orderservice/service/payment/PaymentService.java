package org.vovgoo.orderservice.service.payment;

import org.vovgoo.orderservice.dto.payment.request.AddPaymentRequest;
import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.Payment;

public interface PaymentService {
    Payment processPayment(Order order, AddPaymentRequest paymentRequest);
}

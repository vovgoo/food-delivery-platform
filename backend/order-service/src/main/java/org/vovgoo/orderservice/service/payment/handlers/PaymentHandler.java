package org.vovgoo.orderservice.service.payment.handlers;

import org.vovgoo.orderservice.entity.Order;
import org.vovgoo.orderservice.entity.Payment;
import org.vovgoo.orderservice.entity.enums.PaymentMethod;

public interface PaymentHandler {

    Payment process(Order order);

    PaymentMethod getPaymentMethod();
}
package org.vovgoo.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.orderservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}

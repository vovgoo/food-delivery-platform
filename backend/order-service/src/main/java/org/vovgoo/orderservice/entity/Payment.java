package org.vovgoo.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.vovgoo.orderservice.entity.enums.PaymentMethod;
import org.vovgoo.orderservice.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"order"})
@EqualsAndHashCode(exclude = {"order"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "Необходимо указать способ оплаты")
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @NotNull(message = "Сумма платежа не может быть пустой")
    @DecimalMin(value = "0.01", message = "Сумма платежа должна быть положительной")
    @Digits(integer = 8, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
    private BigDecimal amount;

    @NotNull(message = "Необходимо указать статус платежа")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @CreationTimestamp
    private LocalDateTime paymentDate;
}

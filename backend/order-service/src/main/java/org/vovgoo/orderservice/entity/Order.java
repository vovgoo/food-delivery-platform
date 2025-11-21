package org.vovgoo.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.vovgoo.orderservice.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"items", "payment"})
@EqualsAndHashCode(exclude = {"items", "payment"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Статус заказа не может быть пустым")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreationTimestamp
    @NotNull(message = "Дата создания заказа не может быть пустой")
    private LocalDateTime orderDate;

    @NotNull(message = "ID пользователя не может быть пустым")
    private UUID userId;

    @NotNull(message = "ID адреса доставки не может быть пустым")
    private UUID deliveryAddress;

    @NotNull(message = "ID ресторана не может быть пустым")
    private UUID restaurantId;

    @NotNull(message = "Общая сумма заказа не может быть пустой")
    @DecimalMin(value = "0.00", message = "Сумма заказа должна быть неотрицательной")
    @Digits(integer = 8, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
    private BigDecimal totalPrice;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
}

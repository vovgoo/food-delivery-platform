package org.vovgoo.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"order"})
@EqualsAndHashCode(exclude = {"order"})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Ссылка на заказ не может быть пустой")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @NotNull(message = "ID блюда не может быть пустым")
    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @NotNull(message = "Количество не может быть пустым")
    @Min(value = 1, message = "Количество должно быть не меньше 1")
    @Column(nullable = false)
    private Long quantity;

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.01", message = "Цена должна быть положительной")
    @Digits(integer = 8, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
    private BigDecimal price;
}

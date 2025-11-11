package org.vovgoo.restaurantservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Entity
@Table(name = "dishes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"restaurant"})
@EqualsAndHashCode(exclude = {"restaurant"})
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название блюда не может быть пустым")
    @Size(min = 2, max = 100, message = "Название блюда должно быть от 2 до 100 символов")
    private String name;

    @Size(min = 0, max = 500, message = "Описание блюда должно быть до 500 символов")
    private String description;

    @NotNull(message = "Цена блюда обязательна")
    @DecimalMin(value = "0.01", message = "Цена блюда должна быть больше 0")
    @Digits(integer = 6, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
    private BigDecimal price;

    @URL(message = "URL изображения должен быть валидным")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}

package org.vovgoo.restaurantservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.vovgoo.restaurantservice.entity.enums.DishStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Название блюда не может быть пустым")
    @Size(min = 2, max = 100, message = "Название блюда должно быть от 2 до 100 символов")
    private String name;

    @Size(min = 0, max = 500, message = "Описание блюда должно быть до 500 символов")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private DishImage profileImage;

    @PositiveOrZero(message = "Вес блюда должен быть положительным или нулевым")
    private Integer portionInGrams;

    @PositiveOrZero(message = "Количество белков должно быть положительным или нулевым")
    private Double proteins;

    @PositiveOrZero(message = "Количество жиров должно быть положительным или нулевым")
    private Double fats;

    @PositiveOrZero(message = "Количество углеводов должно быть положительным или нулевым")
    private Double carbohydrates;

    @NotNull(message = "Необходимо указать, острое ли блюдо")
    private Boolean spicy = false;

    @NotNull(message = "Необходимо указать, подходит ли блюдо для веганов")
    private Boolean vegan = false;

    @NotNull(message = "Необходимо указать, подходит ли блюдо для вегетарианцев")
    private Boolean vegetarian = false;

    @NotNull(message = "Цена блюда обязательна")
    @DecimalMin(value = "0.01", message = "Цена блюда должна быть больше 0")
    @Digits(integer = 6, fraction = 2, message = "Цена должна быть числом с максимум 2 знаками после запятой")
    private BigDecimal price;

    @NotNull(message = "Статус ресторана не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DishStatus status = DishStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DishImage> images = new ArrayList<>();
}

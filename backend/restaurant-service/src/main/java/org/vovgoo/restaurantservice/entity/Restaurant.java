package org.vovgoo.restaurantservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.vovgoo.restaurantservice.entity.enums.RestaurantStatus;
import org.vovgoo.validators.phone.Phone;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"dishes"})
@EqualsAndHashCode(exclude = {"dishes"})
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Название ресторана не может быть пустым")
    @Size(min = 2, max = 100, message = "Название ресторана должно быть от 2 до 100 символов")
    private String name;

    @Size(max = 1000, message = "Описание ресторана не должно превышать 1000 символов")
    private String description;

    @NotBlank(message = "Кухня не может быть пустой")
    @Size(min = 2, max = 50, message = "Кухня должна быть от 2 до 50 символов")
    private String cuisine;

    @NotBlank(message = "Адрес не может быть пустым")
    @Size(min = 5, max = 200, message = "Адрес должен быть от 5 до 200 символов")
    private String address;

    @Size(max = 200, message = "URL сайта не должен превышать 200 символов")
    @URL(message = "Некорректный формат сайта")
    private String website;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private RestaurantImage profileImage;

    @Phone
    @NotBlank(message = "Телефон не может быть пустым")
    private String phone;

    @NotNull(message = "Время открытия должно быть указано")
    private LocalTime openingTime;

    @NotNull(message = "Время закрытия должно быть указано")
    private LocalTime closingTime;

    @NotNull(message = "Необходимо указать, доступна ли доставка")
    private Boolean deliveryAvailable;

    @NotNull(message = "Необходимо указать, доступна ли парковка")
    private Boolean parkingAvailable;

    @NotNull(message = "Статус ресторана не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RestaurantStatus status = RestaurantStatus.ACTIVE;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<Dish> dishes = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<RestaurantImage> images = new ArrayList<>();
}

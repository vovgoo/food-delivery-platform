package org.vovgoo.restaurantservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название ресторана не может быть пустым")
    @Size(min = 2, max = 100, message = "Название ресторана должно быть от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Кухня не может быть пустой")
    @Size(min = 2, max = 50, message = "Кухня должна быть от 2 до 50 символов")
    private String cuisine;

    @NotBlank(message = "Адрес не может быть пустым")
    @Size(min = 5, max = 200, message = "Адрес должен быть от 5 до 200 символов")
    private String address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dish> dishes = new ArrayList<>();
}

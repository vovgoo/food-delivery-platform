package org.vovgoo.restaurantservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "dish_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Имя файла не может быть пустым")
    @Size(max = 255, message = "Имя файла не должно превышать 255 символов")
    private String filename;

    @NotNull
    private Boolean isProfile = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;
}
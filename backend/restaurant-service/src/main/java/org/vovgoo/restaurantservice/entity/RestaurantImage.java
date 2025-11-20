package org.vovgoo.restaurantservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Entity
@Table(name = "restaurant_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "URL не может быть пустым")
    @Size(max = 255, message = "URL не должно превышать 255 символов")
    @URL(message = "URL должен быть корректным")
    private String url;

    @NotNull
    private Boolean isProfile = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}

package org.vovgoo.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vovgoo.userservice.entity.enums.AddressStatus;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Страна не может быть пустой")
    @Size(max = 100, message = "Слишком длинная страна")
    @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Страна содержит недопустимые символы")
    private String country;

    @NotBlank(message = "Штат/регион не может быть пустым")
    @Size(max = 100, message = "Слишком длинный штат/регион")
    @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Штат/регион содержит недопустимые символы")
    private String state;

    @NotBlank(message = "Город не может быть пустым")
    @Size(max = 100, message = "Слишком длинный город")
    @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Город содержит недопустимые символы")
    private String city;

    @NotBlank(message = "Улица не может быть пустой")
    @Size(max = 255, message = "Слишком длинная улица")
    @Pattern(regexp = "^[\\p{L}0-9\\s\\-\\.]+$", message = "Улица содержит недопустимые символы")
    private String street;

    @NotBlank(message = "Номер дома обязателен")
    @Size(max = 20, message = "Номер дома слишком длинный")
    @Pattern(regexp = "^[0-9A-Za-z\\-\\/]+$", message = "Номер дома содержит недопустимые символы")
    private String house;

    @Size(max = 10, message = "Слишком длинный корпус")
    @Pattern(regexp = "^[0-9A-Za-z\\-\\/]*$", message = "Корпус содержит недопустимые символы")
    private String building;

    @Size(max = 10, message = "Слишком длинная квартира/офис")
    @Pattern(regexp = "^[0-9A-Za-z\\-\\/]*$", message = "Квартира содержит недопустимые символы")
    private String apartment;

    @Size(max = 500, message = "Инструкция слишком длинная")
    private String deliveryInstructions;

    @NotBlank(message = "ZIP код не может быть пустым")
    @Size(max = 20, message = "ZIP слишком длинный")
    @Pattern(regexp = "^[0-9\\-\\s]+$", message = "ZIP содержит недопустимые символы")
    private String zip;

    @NotNull(message = "Статус адреса не может быть пустым")
    @Enumerated(EnumType.STRING)
    private AddressStatus addressStatus = AddressStatus.ACTIVE;

    @NotNull(message = "Значение по умолчанию не может быть пустым")
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Пользователь обязателен для адреса")
    private User user;
}

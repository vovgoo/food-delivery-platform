package org.vovgoo.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "Улица не может быть пустой")
    @Size(max = 255, message = "Слишком длинная улица")
    @Pattern(regexp = "^[\\p{L}0-9\\s\\-\\.]+$", message = "Улица содержит недопустимые символы")
    private String street;

    @NotBlank(message = "Город не может быть пустым")
    @Size(max = 100, message = "Слишком длинный город")
    @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Город содержит недопустимые символы")
    private String city;

    @NotBlank(message = "ZIP код не может быть пустым")
    @Size(max = 20, message = "ZIP слишком длинный")
    @Pattern(regexp = "^[0-9\\-\\s]+$", message = "ZIP содержит недопустимые символы")
    private String zip;

    @NotBlank(message = "Штат/регион не может быть пустым")
    @Size(max = 100, message = "Слишком длинный штат/регион")
    @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Штат/регион содержит недопустимые символы")
    private String state;

    @NotBlank(message = "Страна не может быть пустой")
    @Size(max = 100, message = "Слишком длинная страна")
    @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Страна содержит недопустимые символы")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

package org.vovgoo.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.vovgoo.enums.user.UserStatus;
import org.vovgoo.userservice.validators.email.domain.AllowedEmailDomain;
import org.vovgoo.validators.phone.Phone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"addresses", "roles", "passwordHash"})
@EqualsAndHashCode(exclude = {"addresses", "roles"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @AllowedEmailDomain
    @Email(message = "Почта должна быть корректной")
    @Size(max = 255, message = "Почта слишком длинная")
    @Column(unique = true)
    private String email;

    @Phone
    @NotBlank(message = "Телефон не может быть пустым")
    @Column(unique = true)
    private String phone;

    @NotBlank(message = "Полное имя не может быть пустым")
    @Size(min = 2, max = 100, message = "Полное имя должно быть от 2 до 100 символов")
    private String fullName;

    @NotNull(message = "День рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotNull(message = "Статус пользователя не может быть пустым")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @NotBlank(message = "Пароль не может быть пустым")
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

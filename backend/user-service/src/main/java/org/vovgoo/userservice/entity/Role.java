package org.vovgoo.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.vovgoo.userservice.entity.enums.RoleType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    private Set<User> users = new HashSet<>();
}

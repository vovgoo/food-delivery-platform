package org.vovgoo.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "email_domains")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String domain;

    @Column(nullable = false)
    private boolean allowed;
}
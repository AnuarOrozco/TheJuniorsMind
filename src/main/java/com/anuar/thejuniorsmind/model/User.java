package com.anuar.thejuniorsmind.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50)
    private String username;


    @Column(nullable = false, unique = true, length = 50)
    @NotBlank
    @Size(max = 50)
    private String email;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(length = 255)
    private String avatarUrl;

    @Column(length = 255)
    private String bio;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}

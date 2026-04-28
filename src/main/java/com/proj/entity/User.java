package com.proj.entity;

import com.proj.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Column(name = "user_id")
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "pw_hash")
    private String pwHash;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;
}

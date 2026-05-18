package com.proj.entity;

import com.proj.constant.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Column(name = "role_id")
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}

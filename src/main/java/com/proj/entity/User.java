package com.proj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
           name = "user_roles",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id")
   )
    private Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<UserBook> userBook;

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }
}

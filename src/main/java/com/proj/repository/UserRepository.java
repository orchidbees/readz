package com.proj.repository;

import com.proj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // TODO: meant to stop lazy loading of roles but doesn't seem to work properly, need to look into how to fix this.
    //  Issue is that roles aren't attached to JWT so authorisation isn't working.
//    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsername(String username);
}

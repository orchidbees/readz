package com.proj.controller;

import com.proj.entity.User;
import com.proj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @GetMapping
    ResponseEntity<List<User>> all() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<User> one(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<User> newUser(@RequestBody User newUser) {
        return new ResponseEntity<>(repository.save(newUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPwHash(newUser.getPwHash());
                    user.setBirthDate(newUser.getBirthDate());
                    return new ResponseEntity<>(repository.save(user), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(repository.save(newUser), HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}

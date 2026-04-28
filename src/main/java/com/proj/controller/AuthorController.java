package com.proj.controller;

import com.proj.entity.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proj.repository.AuthorRepository;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    // TODO: create AuthorService and swap all this out

    private final AuthorRepository repository;

    @GetMapping
    ResponseEntity<List<Author>> all() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Author> one(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Author> newAuthor(@RequestBody Author newAuthor) {
        return new ResponseEntity<>(repository.save(newAuthor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Author> updateAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {
        return repository.findById(id)
                .map(author -> {
                    author.setFullName(newAuthor.getFullName());
                    author.setBirthDate(newAuthor.getBirthDate());
                    return new ResponseEntity<>(repository.save(author), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(repository.save(newAuthor), HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}

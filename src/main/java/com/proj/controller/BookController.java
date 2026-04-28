package com.proj.controller;

import com.proj.dto.BookRequest;
import com.proj.dto.BookResponse;
import com.proj.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    ResponseEntity<Set<BookResponse>> all() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<BookResponse> one(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<BookResponse> add(@RequestBody BookRequest newBook) {
        BookResponse book = bookService.createBook(newBook);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<BookResponse> upsert(@RequestBody BookRequest newBook, @PathVariable Long id) {
        Optional<BookResponse> updatedBook = bookService.updateBookById(newBook, id);

        return updatedBook.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElseGet(() ->
                        new ResponseEntity<>(bookService.createBook(newBook), HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
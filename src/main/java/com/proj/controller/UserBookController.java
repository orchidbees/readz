package com.proj.controller;

import com.proj.auth.AuthUser;
import com.proj.dto.http.UserBookRequest;
import com.proj.dto.http.UserBookResponse;
import com.proj.service.BookImporterService;
import com.proj.service.UserBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserBookController {

    private final UserBookService userBookService;
    private final BookImporterService bookImporterService;

    @GetMapping
    ResponseEntity<List<UserBookResponse>> all(@AuthenticationPrincipal AuthUser user) {
        List<UserBookResponse> allBooks = userBookService.getAllUserBooks(user.getId());
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserBookResponse> one(@AuthenticationPrincipal AuthUser user, @PathVariable Long id) {
        UserBookResponse book = userBookService.getUserBook(id, user.getId());
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<UserBookResponse> newUserBook(@AuthenticationPrincipal AuthUser user,
                                                 @RequestBody UserBookRequest newUserBook) {
        UserBookResponse response = userBookService.createUserBook(newUserBook, user.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserBookResponse> updateUserBook(@AuthenticationPrincipal AuthUser user,
                                                    @RequestBody UserBookRequest newUserBook,
                                                    @PathVariable Long id) {
        UserBookResponse response = userBookService.updateUserBookById(newUserBook, id, user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/import")
    ResponseEntity<List<UserBookResponse>> importBooks(@AuthenticationPrincipal AuthUser user,
                                                       @RequestBody MultipartFile bookImport) {

        List<UserBookResponse> response = bookImporterService.importBooks(user.getId(), bookImport);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}

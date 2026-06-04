package com.proj.service;

import com.proj.dto.http.BookResponse;
import com.proj.dto.http.UserBookResponse;
import com.proj.dto.importer.ImportResult;
import com.proj.entity.Book;
import com.proj.entity.User;
import com.proj.importer.BookImporter;
import com.proj.repository.BookRepository;
import com.proj.repository.UserBookRepository;
import com.proj.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookImporterServiceTest {

    @Mock private BookImporter bookImporter;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserBookRepository userBookRepository;
    @Mock private AuthorService authorService;
    @Mock private MultipartFile file;
    @InjectMocks private BookImporterService bookImporterService;

    private static final Long USER_ID = 1L;


    @Test
    void imported_existing_books_with_isbn_should_be_returned() {
        ImportResult importResult = ImportResult.builder()
                .isbn10("1234")
                .build();

        configureDatabaseMocks();
        when(bookImporter.importBooks(file)).thenReturn(Set.of(importResult));

        List<UserBookResponse> result = bookImporterService.importBooks(USER_ID, file);

        assertThat(result).singleElement()
                .extracting(UserBookResponse::book)
                .extracting(BookResponse::isbn10)
                .isEqualTo("1234");

    }

    @Test
    void imported_missing_books_should_not_be_returned() {
        ImportResult importResult = ImportResult.builder()
                .isbn10("5678")
                .build();

        when(bookRepository.findAllByIsbn10OrIsbn13IsNotNull()).thenReturn(Collections.emptySet());
        when(bookImporter.importBooks(file)).thenReturn(Set.of(importResult));

        List<UserBookResponse> result = bookImporterService.importBooks(USER_ID, file);

        assertThat(result).isEmpty();
    }

    private void configureDatabaseMocks() {
        configureBookRepositoryMock();
        configureUserRepositoryMock();
    }

    private void configureBookRepositoryMock() {
        Book book = new Book();
        book.setIsbn10("1234");

        when(bookRepository.findAllByIsbn10OrIsbn13IsNotNull()).thenReturn(Set.of(book));
    }

    private void configureUserRepositoryMock() {
        User user = new User();
        user.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    }
}

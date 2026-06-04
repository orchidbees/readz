package com.proj.service;

import com.proj.dto.http.BookResponse;
import com.proj.dto.http.UserBookResponse;
import com.proj.dto.importer.ImportResult;
import com.proj.entity.Book;
import com.proj.entity.User;
import com.proj.entity.UserBook;
import com.proj.importer.BookImporter;
import com.proj.repository.BookRepository;
import com.proj.repository.UserBookRepository;
import com.proj.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookImporterService {

    private final BookImporter bookImporter;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final AuthorService authorService;

    public List<UserBookResponse> importBooks(Long userId, MultipartFile userImport) {
        // For now this will default to GR importer but need to consider multiple services. Factory pattern?
        Set<ImportResult> importResults = bookImporter.importBooks(userImport);

        // For simplicity, will only import results that have an ISBN 10 or 13.
        // This should be expanded (e.g. hash of sensible values?) or passed to some sort of matching service.
        Map<String, Book> isbnToBook = createIsbnToBookLookUp();

        Set<BookImport> bookImports = new HashSet<>();

        for (ImportResult importResult : importResults) {
            getIsbn(importResult)
                    .map(isbnToBook::get)
                    .map(book -> new BookImport(book, importResult))
                    .ifPresent(bookImports::add);
        }

        log.info("Successfully matched {}/{} imported user books", bookImports.size(), importResults.size());

        Set<UserBook> userBooks = new HashSet<>();

        for (BookImport bookImport : bookImports) {
            userBooks.add(createUserBook(bookImport, userId));
        }

        userBookRepository.saveAll(userBooks);

        return userBooks.stream().map(this::map).toList();
    }

    private Map<String, Book> createIsbnToBookLookUp() {
        Collection<Book> existingBooks = bookRepository.findAllByIsbn10OrIsbn13IsNotNull();

        return existingBooks.stream()
                .flatMap(book -> Stream.of(
                        book.getIsbn10() != null ? new AbstractMap.SimpleEntry<>(book.getIsbn10(), book) : null,
                        book.getIsbn13() != null ? new AbstractMap.SimpleEntry<>(book.getIsbn13(), book) : null
                ).filter(Objects::nonNull))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private Optional<String> getIsbn(ImportResult importResult) {
        return Optional.ofNullable(importResult.getIsbn10())
                .or(() -> Optional.ofNullable(importResult.getIsbn13()));
    }

    private UserBook createUserBook(BookImport bookImport, Long userId) {
        UserBook userBook = new UserBook();
        userBook.setUser(getUserById(userId));
        userBook.setBook(bookImport.getBook());

        ImportResult importResult = bookImport.getImportResult();
        userBook.setReview(importResult.getReview());
        userBook.setPersonalNotes(importResult.getPrivateNotes());
        userBook.setReadingStatus(importResult.getReadingStatus());
        userBook.setRating(importResult.getUserRating());
        userBook.setReadAt(importResult.getDateRead() == null ? null : Instant.from(importResult.getDateRead()));
        userBook.setAddedAt(importResult.getDateAdded() == null ? null : Instant.from(importResult.getDateAdded()));
        userBook.setTimesRead(importResult.getTimesRead());

        return userBook;
    }

    private UserBookResponse map(UserBook userBook) {
        Book book = userBook.getBook();;
        BookResponse bookResponse = new BookResponse(book.getTitle(),
                authorService.convertToReference(book.getAuthors()),
                book.getIsbn10(),
                book.getIsbn13(),
                book.getYearPublished());

        return new UserBookResponse(bookResponse,
                userBook.getReadingStatus(),
                userBook.getRating(),
                userBook.getTimesRead(),
                userBook.getReview(),
                userBook.getPersonalNotes(),
                userBook.getAddedAt(),
                userBook.getUpdatedAt(),
                userBook.getReadAt());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class BookImport {
        private Book book;
        private ImportResult importResult;
    }
}

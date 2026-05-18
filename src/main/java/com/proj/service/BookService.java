package com.proj.service;

import com.proj.dto.http.AuthorReference;
import com.proj.dto.http.BookRequest;
import com.proj.dto.http.BookResponse;
import com.proj.entity.Author;
import com.proj.entity.Book;
import com.proj.exception.BookNotFoundException;
import com.proj.mapper.BookMapper;
import com.proj.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookResponse createBook(BookRequest bookRequest) {
        Set<Author> authors = authorService.fetchAuthors(bookRequest.authorIds());
        Set<AuthorReference> authorReferences = authorService.convertToReference(authors);

        Book book = BookMapper.toEntity(bookRequest, authors);
        return BookMapper.toDto(bookRepository.save(book), authorReferences);
    }

    public Optional<BookResponse> updateBookById(BookRequest bookRequest, Long bookId) {
        return bookRepository.findById(bookId)
                .map(book -> {
                    book.setTitle(bookRequest.title());
                    book.setAuthors(authorService.fetchAuthors(bookRequest.authorIds()));
                    book.setIsbn10(bookRequest.isbn10());
                    book.setIsbn13(bookRequest.isbn13());
                    book.setYearPublished(bookRequest.yearPublished());

                    Book updatedBook = bookRepository.save(book);
                    return BookMapper.toDto(updatedBook, authorService.convertToReference(updatedBook.getAuthors()));
                });
    }

    public Set<BookResponse> getBooks() {
        Set<Book> books = new HashSet<>(bookRepository.findAll());

        return books.stream()
                .map(book -> BookMapper.toDto(book, authorService.convertToReference(book.getAuthors())))
                .collect(Collectors.toSet());
    }

    public BookResponse getBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        return BookMapper.toDto(book, authorService.convertToReference(book.getAuthors()));
    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}

package com.proj.service;

import com.proj.dto.http.AuthorReference;
import com.proj.dto.http.BookResponse;
import com.proj.dto.http.UserBookRequest;
import com.proj.dto.http.UserBookResponse;
import com.proj.entity.Book;
import com.proj.entity.User;
import com.proj.entity.UserBook;
import com.proj.exception.BookNotFoundException;
import com.proj.repository.BookRepository;
import com.proj.repository.UserBookRepository;
import com.proj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserBookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final AuthorService authorService;

    public List<UserBookResponse> getAllUserBooks(Long userId) {
        return userBookRepository.findByUserId(userId).stream()
                .map(userBook -> mapToResponse(userBook, userBook.getBook()))
                .toList();
    }

    public UserBookResponse getUserBook(Long userBookId, Long userId) {
        return userBookRepository.findByIdAndUserId(userBookId, userId)
                .map(userBook -> mapToResponse(userBook, userBook.getBook()))
                .orElseThrow(() -> new RuntimeException(
                        "User book with id: " + userBookId + " not found for user id: " + userId));
    }

    public UserBookResponse createUserBook(UserBookRequest bookRequest, Long userId) {
        Book book = getBookById(bookRequest.bookId());
        User user = getUserById(userId);

        UserBook userBook = new UserBook();
        userBook.setBook(book);
        userBook.setUser(user);
        userBook.setReadingStatus(bookRequest.readingStatus());
        userBook.setReview(bookRequest.review());
        userBook.setPersonalNotes(bookRequest.personalNotes());
        userBook.setAddedAt(Instant.now());

        userBookRepository.save(userBook);

        return mapToResponse(userBook, book);
    }

    public UserBookResponse updateUserBookById(UserBookRequest bookRequest, Long userBookId, Long userId) {
        Book book = getBookById(bookRequest.bookId());
        User user = getUserById(userId);

        return userBookRepository.findByIdAndUserId(userBookId, user.getId())
                .map(userBook -> {
                    userBook.setBook(book);
                    userBook.setUser(user);
                    userBook.setRating(bookRequest.rating());
                    userBook.setReview(bookRequest.review());
                    userBook.setReadingStatus(bookRequest.readingStatus());
                    userBook.setPersonalNotes(bookRequest.personalNotes());
                    userBook.setUpdatedAt(Instant.now());

                    UserBook updatedBook = userBookRepository.save(userBook);
                    return mapToResponse(updatedBook, book);
                }).orElseThrow(() -> new RuntimeException(
                        "User book with id: " + userBookId + " not found for user id: " + userId));
    }

    public void deleteUserBook() {

    }

    private Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private UserBookResponse mapToResponse(UserBook userBook, Book book) {
        Set<AuthorReference> authors = authorService.convertToReference(book.getAuthors());

        BookResponse bookResponse = new BookResponse(book.getTitle(),
                authors,
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
}

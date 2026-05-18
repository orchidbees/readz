//package com.proj.service;
//
//import com.proj.constant.ReadingStatus;
//import com.proj.dto.http.UserBookResponse;
//import com.proj.entity.Author;
//import com.proj.entity.Book;
//import com.proj.entity.User;
//import com.proj.entity.UserBook;
//import com.proj.repository.BookRepository;
//import com.proj.repository.UserBookRepository;
//import com.proj.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class UserBookServiceTest {
//
//    @Mock
//    UserBookRepository userBookRepository;
//    @Mock
//    BookRepository bookRepository;
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    AuthorService authorService;
//    @InjectMocks
//    UserBookService service;
//
//    private static final String USERNAME = "test";
//    private static final String BOOK_TITLE = "Crime & Punishment";
//    private static final String BOOK_AUTHOR = "Fyodor Dostoevsky";
//    private static final LocalDate AUTHOR_DOB = LocalDate.parse("1821-11-11");
//    private static final String ISBN_10 = "0140449132";
//    private static final String ISBN_13 = "9780553211757";
//    private static final int RATING = 5;
//    private static final String REVIEW = "some review";
//    private static final String PERSONAL_NOTES = "some notes";
//    private static final ReadingStatus READING_STATUS = ReadingStatus.READ;
//    private static final int TIMES_READ = 1;
//
//    @Test
//    void should_map_to_user_book_response_when_retrieving_book() {
//        User user = new User();
//        user.setId(1L);
//
//        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
//        when(userBookRepository.findByUserId(user.getId())).thenReturn(List.of(createUserBook(user)));
//
//        List<UserBookResponse> result = service.getAllUserBooks(USERNAME);
//
//        System.out.println(":)");
//    }
//
//    @Test
//    void creating_new_user_book_should_set_added_at_time() {
//
//    }
//
//    @Test
//    void should_throw_exception_if_username_not_found() {
//        assertThatThrownBy(() -> service.getAllUserBooks(USERNAME))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("Username not found: %s", USERNAME);
//    }
//
//    @Test
//    void get_user_book_should_throw_exception_if_user_book_not_found() {
//        User user = new User();
//        user.setId(1L);
//
//        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
//
//        assertThatThrownBy(() -> service.getUserBook(2L, USERNAME))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("User book for username %s not found with id: %d", USERNAME, 2L);
//    }
//
//    @Test
//    void should_update_user_books_by_id() {
//
//    }
//
//    @Test
//    void should_throw_exception_if_book_not_found() {
//
//    }
//
//
//    private UserBook createUserBook(User user) {
//        Author author = new Author();
//        author.setFullName(BOOK_AUTHOR);
//        author.setBirthDate(AUTHOR_DOB);
//
//        Book book = new Book();
//        book.setTitle(BOOK_TITLE);
//        book.setIsbn13(ISBN_13);
//        book.setIsbn10(ISBN_10);
//        book.setAuthors(Set.of(author));
//
//        UserBook userBook = new UserBook();
//        userBook.setUser(user);
//        userBook.setBook(book);
//        userBook.setRating(RATING);
//        userBook.setReview(REVIEW);
//        userBook.setPersonalNotes(PERSONAL_NOTES);
//        userBook.setReadingStatus(READING_STATUS);
//        userBook.setTimesRead(TIMES_READ);
//
//        return userBook;
//    }
//}

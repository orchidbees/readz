package com.proj;

import com.proj.dto.BookRequest;
import com.proj.entity.Author;
import com.proj.entity.Book;
import com.proj.repository.AuthorRepository;
import com.proj.repository.BookRepository;
import com.proj.service.AuthorService;
import com.proj.service.BookService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = { AuthorRepository.class, BookRepository.class, BookService.class}
)
public class BookAuthorIT {

    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine3.22")
            .withDatabaseName("readz");

    @Autowired AuthorRepository authorRepository;
    @Autowired BookRepository bookRepository;
    @Autowired BookService bookService;
    @MockitoBean AuthorService authorService;

    @BeforeAll
    static void init() {
        postgres.start();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> true);
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @Test
    void book_author_should_be_linked_if_author_exists() {
        Author author = new Author();
        author.setFullName("Fyodor Dostoevsky");
        author.setBirthDate(LocalDate.parse("1821-11-11"));

        Author savedAuthor = authorRepository.save(author);

        bookService.createBook(new BookRequest(
                "Crime & Punishment",
                Set.of(savedAuthor.getId()), null, null, null));

        List<Book> savedBook = bookRepository.findByTitle("Crime & Punishment");

        assertThat(savedBook)
                .singleElement()
                .extracting(Book::getAuthors, as(InstanceOfAssertFactories.SET))
                .singleElement()
                .isEqualTo(author.getId());
    }

    private Long insertAuthor() {
        var sql = "INSERT INTO authors(full_name, birth_date) VALUES(?,?)";

        try (var conn = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "test");
            pstmt.setDate(2, Date.valueOf("1970-01-01"));

            int insertedRow = pstmt.executeUpdate();

            if (insertedRow > 0) {
                var rs = pstmt.getGeneratedKeys();

                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            // noop
        }
        throw new RuntimeException("Test data for author was not successfully inserted.");
    }
}

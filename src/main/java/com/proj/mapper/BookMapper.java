package com.proj.mapper;

import com.proj.dto.AuthorReference;
import com.proj.dto.BookRequest;
import com.proj.dto.BookResponse;
import com.proj.entity.Author;
import com.proj.entity.Book;

import java.util.Set;

public class BookMapper {

    public static BookResponse toDto(Book book, Set<AuthorReference> authors) {
        return new BookResponse(book.getTitle(),
                authors,
                book.getIsbn10(),
                book.getIsbn13(),
                book.getYearPublished());
    }

    public static Book toEntity(BookRequest bookRequest, Set<Author> authors) {
        Book book = new Book();
        book.setTitle(bookRequest.title());
        book.setAuthors(authors);
        book.setIsbn10(bookRequest.isbn10());
        book.setIsbn13(bookRequest.isbn13());
        book.setYearPublished(bookRequest.yearPublished());
        return book;
    }
}

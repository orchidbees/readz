package com.proj.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Column(name = "book_id")
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String title;

    // TODO: update to remove JsonManagedReference annotation
    // Wanted to see if it'd solve the infinite recursion issue as detailed here:
    // https://stackoverflow.com/questions/47693110/could-not-write-json-infinite-recursion-stackoverflowerror-nested-exception
    // and it does, but I think a DTO is the better way forwards...
    // Allows me to not include IDs. I don't think it's the worst idea in the world but can have some
    // security implications. See:
    // 
    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @Column(name = "isbn_10")
    private String isbn10;

    @Column(name = "isbn_13")
    private String isbn13;

    @Column(name = "year_published")
    private Year yearPublished;

    @OneToMany
    @JoinColumn(name = "book_id")
    private Set<UserBook> userBook;

    public Set<Author> getAuthors() {
        if (authors == null) {
            authors = new HashSet<>();
        }
        return authors;
    }
}
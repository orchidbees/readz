package com.proj.repository;

import com.proj.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitle(String title);
    Collection<Book> findAllByIsbn10OrIsbn13IsNotNull();
}

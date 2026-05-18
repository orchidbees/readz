package com.proj.repository;

import com.proj.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    List<UserBook> findByUserId(Long userId);
    Optional<UserBook> findByIdAndUserId(Long userBookId, Long userId);
}

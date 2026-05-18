package com.proj.entity;

import com.proj.constant.ReadingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_books")
@Getter
@Setter
public class UserBook {

    @Column(name = "user_book_id")
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "reading_status")
    @Enumerated(EnumType.STRING)
    private ReadingStatus readingStatus;

    private Integer rating;

    @Column(name = "date_added")
    private Instant addedAt;

    @Column(name = "date_updated")
    private Instant updatedAt;

    @Column(name = "date_read")
    private Instant readAt;

    @Column(name = "times_read")
    private Integer timesRead;

    private String review;

    @Column(name = "personal_notes")
    private String personalNotes;

}

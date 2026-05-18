package com.proj.dto.http;

import com.proj.constant.ReadingStatus;

import java.time.Instant;

public record UserBookResponse (
    BookResponse book,
    ReadingStatus readingStatus,
    Integer rating,
    Integer timesRead,
    String review,
    String personalNotes,
    Instant addedAt,
    Instant updatedAt,
    Instant readAt){}
package com.proj.dto.http;

import com.proj.constant.ReadingStatus;

public record UserBookRequest(
        Long bookId,
        ReadingStatus readingStatus,
        Integer rating,
        Integer timesRead,
        String review,
        String personalNotes) {}

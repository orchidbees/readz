package com.proj.dto;

import java.time.Year;
import java.util.Set;

public record BookRequest(
        String title,
        Set<Long> authorIds,
        String isbn10,
        String isbn13,
        Year yearPublished) {}

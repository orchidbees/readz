package com.proj.dto;

import java.time.Year;
import java.util.Set;

public record BookResponse (
        String title,
        Set<AuthorReference> authors,
        String isbn10,
        String isbn13,
        Year yearPublished) {}
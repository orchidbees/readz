package com.proj.dto.http;

import java.time.LocalDate;

public record AuthorRequest(
        String fullName,
        LocalDate dateOfBirth) {}

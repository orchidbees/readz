package com.proj.dto;

import java.time.LocalDate;

public record AuthorRequest(
        String fullName,
        LocalDate dateOfBirth) {}

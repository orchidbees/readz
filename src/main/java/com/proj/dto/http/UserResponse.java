package com.proj.dto.http;

import java.time.LocalDate;

public record UserResponse(
        String username,
        LocalDate birthDate) {}

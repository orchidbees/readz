package com.proj.dto;

import java.time.LocalDate;

public record UserResponse(
        String username,
        LocalDate birthDate) {}

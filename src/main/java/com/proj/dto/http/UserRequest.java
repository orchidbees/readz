package com.proj.dto.http;

import java.time.LocalDate;

public record UserRequest(
        String username,
        String password,
        LocalDate birthDate) {}

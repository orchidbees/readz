package com.proj.dto;

import java.time.LocalDate;

public record UserRequest(
        String username,
        String password,
        LocalDate birthDate) {}

package com.proj.dto.http;

public record AuthResponse (
        String token,
        String name,
        Long expiry) {}

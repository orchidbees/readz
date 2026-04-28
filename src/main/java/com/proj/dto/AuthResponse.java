package com.proj.dto;

public record AuthResponse (
        String token,
        String name,
        Long expiry) {}

package com.proj.dto.http;

public record AuthRequest(
        String username,
        String password) {}

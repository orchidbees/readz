package com.proj.controller;

import com.proj.dto.http.AuthRequest;
import com.proj.dto.http.AuthResponse;
import com.proj.dto.http.UserRequest;
import com.proj.dto.http.UserResponse;
import com.proj.service.AuthService;
import com.proj.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRegistrationService registrationService;

    @PostMapping("/token")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }

    @PostMapping("/register")
    ResponseEntity<UserResponse> register(@RequestBody UserRequest newUser) {
        return new ResponseEntity<>(registrationService.register(newUser), HttpStatus.CREATED);
    }
}

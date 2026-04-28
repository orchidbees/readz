package com.proj.service;

import com.proj.constant.Role;
import com.proj.dto.UserRequest;
import com.proj.dto.UserResponse;
import com.proj.entity.User;
import com.proj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.username());
        user.setPwHash(passwordEncoder.encode(userRequest.password()));
        user.setBirthDate(userRequest.birthDate());
        user.setRole(Role.ROLE_USER);

        User newUser = userRepository.save(user);

        return new UserResponse(newUser.getUsername(), newUser.getBirthDate());
    }
}

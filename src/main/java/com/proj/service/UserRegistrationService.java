package com.proj.service;

import com.proj.constant.RoleType;
import com.proj.dto.http.UserRequest;
import com.proj.dto.http.UserResponse;
import com.proj.entity.Role;
import com.proj.entity.User;
import com.proj.repository.RoleRepository;
import com.proj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.username());
        user.setPwHash(passwordEncoder.encode(userRequest.password()));
        user.setBirthDate(userRequest.birthDate());

        Role role = roleRepository.getRoleByType(RoleType.ROLE_USER);
        user.setRoles(Set.of(role));

        User newUser = userRepository.save(user);

        return new UserResponse(newUser.getUsername(), newUser.getBirthDate());
    }
}

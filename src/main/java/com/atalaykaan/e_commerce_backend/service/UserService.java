package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.dto.request.CreateUserRequest;
import com.atalaykaan.e_commerce_backend.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.exception.UserNotFoundException;
import com.atalaykaan.e_commerce_backend.mapper.UserMapper;
import com.atalaykaan.e_commerce_backend.model.User;
import com.atalaykaan.e_commerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserDTO findByEmail(String email) {

        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return userMapper.toDTO(foundUser);
    }

    public UserDTO save(CreateUserRequest createUserRequest) {

        User createdUser = userRepository.save(
                User.builder()
                        .firstName(createUserRequest.getFirstName())
                        .lastName(createUserRequest.getLastName())
                        .email(createUserRequest.getEmail())
                        .password(passwordEncoder.encode(createUserRequest.getPassword()))
                        .phone(createUserRequest.getPhone())
                        .authorities(createUserRequest.getAuthorities())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return userMapper.toDTO(createdUser);
    }
}

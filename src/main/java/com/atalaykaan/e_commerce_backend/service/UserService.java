package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.dto.request.AuthRequest;
import com.atalaykaan.e_commerce_backend.dto.request.CreateUserRequest;
import com.atalaykaan.e_commerce_backend.dto.request.UpdateUserRequest;
import com.atalaykaan.e_commerce_backend.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.exception.UserNotFoundException;
import com.atalaykaan.e_commerce_backend.exception.UserWithEmailAlreadyExistsException;
import com.atalaykaan.e_commerce_backend.mapper.UserMapper;
import com.atalaykaan.e_commerce_backend.model.User;
import com.atalaykaan.e_commerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserDTO save(CreateUserRequest createUserRequest) {

        if(userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {

            throw new UserWithEmailAlreadyExistsException("User already exists with email: " + createUserRequest.getEmail());
        }

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

    public String authenticateAndGenerateToken(AuthRequest authRequest) {

        authenticate(authRequest.getEmail(), authRequest.getPassword());

        return jwtService.generateToken(authRequest.getEmail());
    }

    private void authenticate(String email, String password) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

        } catch (UsernameNotFoundException ex) {

            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        catch (BadCredentialsException ex) {

            throw new BadCredentialsException("Invalid credentials");
        }
    }

    public List<UserDTO> findAll() {

        List<UserDTO> userDTOList = userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();

        if(userDTOList.isEmpty()) {

            throw new UserNotFoundException("No users were found");
        }

        return userDTOList;
    }

    public UserDTO findById(Long id) {

        UserDTO userDTO = userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return userDTO;
    }

    public UserDTO findByEmail(String email) {

        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return userMapper.toDTO(foundUser);
    }

    public UserDTO updateById(Long id, UpdateUserRequest updateUserRequest) {

        User foundUser = userRepository.findById(id)
                .map(user -> {
                    updateIfNotNull(updateUserRequest.getFirstName(), user::setFirstName);
                    updateIfNotNull(updateUserRequest.getLastName(), user::setLastName);
                    updateIfNotNull(updateUserRequest.getEmail(), user::setEmail);
                    updateIfNotNull(updateUserRequest.getPassword(), password -> user.setPassword(passwordEncoder.encode(password)));
                    updateIfNotNull(updateUserRequest.getPhone(), user::setPhone);
                    updateIfNotNull(updateUserRequest.getAuthorities(), user::setAuthorities);

                    return user;
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        User savedUser = userRepository.save(foundUser);

        return userMapper.toDTO(savedUser);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {

        if(value != null) {

            setter.accept(value);
        }
    }

    public void deleteById(Long id) {

        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.deleteById(id);
    }

    public void deleteAll() {

        if(userRepository.findAll().isEmpty()) {

            throw new UserNotFoundException("No users were found");
        }

        userRepository.deleteAll();
    }
}

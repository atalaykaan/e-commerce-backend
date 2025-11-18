package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.model.dto.request.auth.AuthRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.create.CreateUserRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateUserRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.exception.UserNotFoundException;
import com.atalaykaan.e_commerce_backend.exception.UserWithEmailAlreadyExistsException;
import com.atalaykaan.e_commerce_backend.mapper.UserMapper;
import com.atalaykaan.e_commerce_backend.model.entity.User;
import com.atalaykaan.e_commerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Transactional
    public UserDTO saveUser(CreateUserRequest createUserRequest) {

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

        UserDTO userDTO = userMapper.toDTO(createdUser);

        return userDTO;
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

    public UserDTO findUserByEmail(String email) {

        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        UserDTO userDTO = userMapper.toDTO(foundUser);

        return userDTO;
    }

    public UserDTO findUserById(UUID id) {

        UserDTO userDTO = userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return userDTO;
    }

    public List<UserDTO> findAllUsers() {

        List<UserDTO> userDTOList = userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();

        if(userDTOList.isEmpty()) {

            throw new UserNotFoundException("No users were found");
        }

        return userDTOList;
    }

    @Transactional
    public UserDTO updateUserById(UUID id, UpdateUserRequest updateUserRequest) {

        User foundUser = userRepository.findById(id)
                .map(user -> {
                    updateIfExists(updateUserRequest.getFirstName(), user::setFirstName);
                    updateIfExists(updateUserRequest.getLastName(), user::setLastName);
                    updateIfExists(updateUserRequest.getEmail(), user::setEmail);
                    updateIfExists(updateUserRequest.getPassword(), password -> user.setPassword(passwordEncoder.encode(password)));
                    updateIfExists(updateUserRequest.getPhone(), user::setPhone);
                    updateIfExists(updateUserRequest.getAuthorities(), user::setAuthorities);

                    return user;
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        User savedUser = userRepository.save(foundUser);

        UserDTO userDTO = userMapper.toDTO(savedUser);

        return userDTO;
    }

    @Transactional
    public void deleteUserById(UUID id) {

        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllUsers() {

        if(userRepository.findAll().isEmpty()) {

            throw new UserNotFoundException("No users were found");
        }

        userRepository.deleteAll();
    }
}

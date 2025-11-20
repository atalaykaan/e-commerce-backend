package com.atalaykaan.e_commerce_backend.domain.user.controller;

import com.atalaykaan.e_commerce_backend.domain.auth.dto.AuthRequest;
import com.atalaykaan.e_commerce_backend.domain.user.dto.CreateUserRequest;
import com.atalaykaan.e_commerce_backend.domain.user.dto.UpdateUserRequest;
import com.atalaykaan.e_commerce_backend.domain.user.dto.UserDTO;
import com.atalaykaan.e_commerce_backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.common.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_USERS)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

        UserDTO savedUserDTO = userService.saveUser(createUserRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUserDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@Valid @RequestBody AuthRequest authRequest) {

        String token = userService.authenticateAndGenerateToken(authRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @GetMapping
    public ResponseEntity<UserDTO> findCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        UserDTO userDTO = userService.findUserByEmail(email);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable UUID id) {

        UserDTO userDTO = userService.findUserById(id);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> findAllUsers() {

        List<UserDTO> userDTOList = userService.findAllUsers();

        return ResponseEntity.ok(userDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {

        UserDTO userDTO = userService.updateUserById(id, updateUserRequest);

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {

        userService.deleteAllUsers();

        return ResponseEntity.noContent().build();
    }
}

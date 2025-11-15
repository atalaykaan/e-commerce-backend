package com.atalaykaan.e_commerce_backend.controller;

import com.atalaykaan.e_commerce_backend.model.dto.request.auth.AuthRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.create.CreateUserRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateUserRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_USERS)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

        UserDTO savedUserDTO = userService.save(createUserRequest);

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
    public ResponseEntity<List<UserDTO>> findAllUsers() {

        List<UserDTO> userDTOList = userService.findAll();

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable UUID id) {

        UserDTO userDTO = userService.findById(id);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {

        UserDTO userDTO = userService.findByEmail(email);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {

        UserDTO userDTO = userService.updateById(id, updateUserRequest);

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {

        userService.deleteAll();

        return ResponseEntity.noContent().build();
    }
}

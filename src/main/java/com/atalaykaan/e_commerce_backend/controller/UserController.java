package com.atalaykaan.e_commerce_backend.controller;

import com.atalaykaan.e_commerce_backend.dto.request.AuthRequest;
import com.atalaykaan.e_commerce_backend.dto.request.CreateUserRequest;
import com.atalaykaan.e_commerce_backend.dto.request.UpdateUserRequest;
import com.atalaykaan.e_commerce_backend.dto.response.UserDTO;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

        UserDTO createdUserDTO = userService.save(createUserRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUserDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@Valid @RequestBody AuthRequest authRequest) {

        String token = userService.authenticateAndGenerateToken(authRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {

        List<UserDTO> userDTOList = userService.findAll();

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {

        UserDTO userDTO = userService.findById(id);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable String email) {

        UserDTO userDTO = userService.findByEmail(email);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateById(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {

        UserDTO userDTO = userService.updateById(id, updateUserRequest);

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {

        userService.deleteAll();

        return ResponseEntity.noContent().build();
    }
}

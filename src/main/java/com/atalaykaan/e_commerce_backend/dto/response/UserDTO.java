package com.atalaykaan.e_commerce_backend.dto.response;

import com.atalaykaan.e_commerce_backend.model.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Set<Role> roles;

    private LocalDateTime createdAt;
}

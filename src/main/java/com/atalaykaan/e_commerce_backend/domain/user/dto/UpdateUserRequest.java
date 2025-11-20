package com.atalaykaan.e_commerce_backend.domain.user.dto;

import com.atalaykaan.e_commerce_backend.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Set<Role> authorities;
}

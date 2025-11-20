package com.atalaykaan.e_commerce_backend.domain.user.dto;

import com.atalaykaan.e_commerce_backend.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Set<Role> authorities;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

package com.atalaykaan.e_commerce_backend.dto.response;

import com.atalaykaan.e_commerce_backend.model.enums.Role;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Set<Role> authorities;

    private LocalDateTime createdAt;

    public UserDTO() {

    }

    public UserDTO(Long id, String firstName, String lastName, String email, String password, String phone, Set<Role> authorities, LocalDateTime createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.authorities = authorities;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(email, userDTO.email) && Objects.equals(password, userDTO.password) && Objects.equals(phone, userDTO.phone) && Objects.equals(authorities, userDTO.authorities) && Objects.equals(createdAt, userDTO.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, phone, authorities, createdAt);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", authorities=" + authorities +
                ", createdAt=" + createdAt +
                '}';
    }
}

package com.atalaykaan.e_commerce_backend.domain.user.mapper;

import com.atalaykaan.e_commerce_backend.domain.user.model.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.domain.user.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);
}

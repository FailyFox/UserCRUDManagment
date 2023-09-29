package com.usercrudmanagment.mapper;

import com.usercrudmanagment.dto.UserRequestDto;
import com.usercrudmanagment.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    UserRequestDto toDto(User user);
    User toUser(UserRequestDto userRequestDto);
}
package com.payment.user.mapper;

import com.payment.user.dto.UserDto;
import com.payment.user.entity.User;

public class UserMapper
{

    public static UserDto mapToUserDto(User user, UserDto userDto){
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public static User mapToUser( UserDto userDto, User user){
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }
}

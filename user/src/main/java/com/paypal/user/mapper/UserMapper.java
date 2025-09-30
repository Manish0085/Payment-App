package com.paypal.user.mapper;

import com.paypal.user.dto.UserDto;
import com.paypal.user.entity.User;

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

package com.paypal.user.service;

import com.paypal.user.dto.UserDto;

import java.util.List;

public interface IUserService {


    void createUser(UserDto userDto);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();
}

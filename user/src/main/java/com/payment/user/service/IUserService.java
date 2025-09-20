package com.payment.user.service;

import com.payment.user.dto.UserDto;
import com.payment.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {


    void createUser(UserDto userDto);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();
}

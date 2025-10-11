package com.paypal.user.service;

import com.paypal.user.dto.UserDto;
import com.paypal.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
}

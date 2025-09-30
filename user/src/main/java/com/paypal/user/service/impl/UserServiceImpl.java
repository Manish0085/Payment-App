package com.paypal.user.service.impl;

import com.paypal.user.dto.UserDto;
import com.paypal.user.entity.User;
import com.paypal.user.exception.UserAlreadyExistsException;
import com.paypal.user.exception.UserNotFoundException;
import com.paypal.user.mapper.UserMapper;
import com.paypal.user.repository.UserRepo;
import com.paypal.user.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private UserRepo userRepo;


    public UserServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public void createUser(UserDto userDto) {
        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(
                    "User Already Registered with " + userDto.getEmail() + " email"
            );
        }
        userRepo.save(UserMapper.mapToUser(userDto, new User()));
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return UserMapper.mapToUserDto(user, new UserDto());
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> UserMapper.mapToUserDto(user, new UserDto()))
                .toList(); // if using Java 16+, else collect(Collectors.toList())
    }


}

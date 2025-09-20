package com.payment.user.controller;

import com.payment.user.constants.UserConstants;
import com.payment.user.dto.ResponseDto;
import com.payment.user.dto.UserDto;
import com.payment.user.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private IUserService iUserService;

    public UserController(IUserService iUserService){
        this.iUserService = iUserService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createUser(@RequestBody UserDto userDto){
        iUserService.createUser(userDto);
        return ResponseEntity
                .ok(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        UserDto user = iUserService.getUserById(id);
        return ResponseEntity
                .ok(user);

    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(@PathVariable Long id){
        List<UserDto> users = iUserService.getAllUsers();
        return ResponseEntity
                .ok(users);

    }

}

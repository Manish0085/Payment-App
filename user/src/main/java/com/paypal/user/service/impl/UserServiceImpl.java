package com.paypal.user.service.impl;

import com.paypal.user.client.WalletClient;
import com.paypal.user.dto.CreateWalletRequest;
import com.paypal.user.dto.UserDto;
import com.paypal.user.entity.User;
import com.paypal.user.exception.UserAlreadyExistsException;
import com.paypal.user.exception.UserNotFoundException;
import com.paypal.user.mapper.UserMapper;
import com.paypal.user.repository.UserRepo;
import com.paypal.user.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{

    private UserRepo userRepo;
    private final WalletClient walletClient;

    public UserServiceImpl(UserRepo userRepo, WalletClient walletClient){
        this.userRepo = userRepo;
        this.walletClient = walletClient;
    }



    @Override
    public User createUser(User user) {
        User savedUser = userRepo.save(user);
        try {
            CreateWalletRequest request = new CreateWalletRequest();
            request.setUserId(savedUser.getId());
            request.setCurrency("INR");
            walletClient.createWallet(request);
        } catch (Exception ex) {
            userRepo.deleteById(savedUser.getId()); // rollback
            throw new RuntimeException("Wallet creation failed, user rolled back", ex);
        }
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
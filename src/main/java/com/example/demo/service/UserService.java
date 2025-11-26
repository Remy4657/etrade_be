package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.user.UserEntity;

@Service
public interface UserService {
    UserEntity createUserEntity(UserEntity user);

    List<UserEntity> getAllUsers();

    List<UserEntity> getUserNameEntity(UserEntity user);

    UserEntity findByUserNameAndUserEmail(String userName, String userEmail);

}

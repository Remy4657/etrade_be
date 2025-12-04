package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.user.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/user")
public class UserCRUDController {
    // @Autowired
    // private UserService userService;

    // @PostMapping("/add")
    // public UserEntity postMethodName(@RequestBody UserEntity userEntity) {
    // return userService.createUserEntity(userEntity);
    // }

    // @GetMapping("/search")
    // public UserEntity searchUser(@RequestParam String name, @RequestParam String
    // email) {
    // return userService.findByUserNameAndUserEmail(name, email);
    // }

    // @GetMapping("/getAll")
    // public Page<UserEntity> getAll(
    // @RequestParam(defaultValue = "0") int page,
    // @RequestParam(defaultValue = "10") int size,
    // @RequestParam(defaultValue = "id") String sort,
    // @RequestParam(defaultValue = "asc") String direction) {
    // Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ?
    // Sort.Direction.ASC : Sort.Direction.DESC;
    // Sort sortBy = Sort.by(sortDirection, sort);
    // Pageable pageble = PageRequest.of(page, size, sortBy);
    // return userService.findAllUsers(pageble);
    // }

}

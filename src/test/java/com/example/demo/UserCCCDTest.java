package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.entity.user.CCCDEntity;

import com.example.demo.repository.CCCDRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
@SpringBootTest

public class UserCCCDTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CCCDRepository cccdRepository;

    @Test
    @Transactional
    @Rollback(false)
    void oneToOneTest() {
        UserEntity user = new UserEntity();
        CCCDEntity cccd = new CCCDEntity();

        user.setUserName("Tips CCCD 2");
        user.setUserEmail("cccd012@g.com");

        cccd.setNumberCCCD("11122222");
        user.setCccd(cccd);

        userRepository.save(user);
    }
}

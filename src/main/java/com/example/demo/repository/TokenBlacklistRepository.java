package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.user.TokenBlacklistEntity;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklistEntity, Long> {
    boolean existsByToken(String token);

    int deleteByExpiredAtBefore(LocalDateTime now);

}
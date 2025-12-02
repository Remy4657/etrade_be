package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.feed.FeedEntity;

public interface FeedRepository extends JpaRepository<FeedEntity, Long> {

    
} 
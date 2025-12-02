package com.example.demo.entity.feed;

import com.example.demo.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "java_feed_001")
public class FeedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedId", nullable = false)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob // use with long text
    @Column(nullable = false)
    private String description;

    //@ManyToOne(cascade = CascadeType.ALL, optional = false)
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false) // foreign key FeedEntity
    private UserEntity user;
}
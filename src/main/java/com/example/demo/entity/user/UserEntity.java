package com.example.demo.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

import com.example.demo.entity.RoleEntity;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    // Many-to-Many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), // FK của User
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK của Role
    )
    private List<RoleEntity> roleList;
}

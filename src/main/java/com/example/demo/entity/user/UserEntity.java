package com.example.demo.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.entity.user.RoleEntity;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column
    private String password;

    @Column(nullable = false)
    private String email;
    private String provider; // GOOGLE, LOCAL

    private String providerId; // google sub

    // Many-to-Many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), // FK của User
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK của Role
    )
    private List<RoleEntity> roleList = new ArrayList<>();
}

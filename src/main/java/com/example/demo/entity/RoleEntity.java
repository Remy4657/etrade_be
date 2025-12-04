package com.example.demo.entity;

import java.util.List;

import com.example.demo.entity.user.UserEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roleList")
    @ToString.Exclude
    private List<UserEntity> userList;
}

package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.user.UserEntity;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    // Page<UserEntity> findByUserName(String username, Pageable pageable);

    // UserEntity findByUserNameAndUserEmail(String userName, String userEmail);

    // // where userName =
    // List<UserEntity> findByUserName(String userName);

    // // where id < 1
    // List<UserEntity> findByIdLessThan(Long id);

    // // RAW SQL
    // @Query("select u from UserEntity u where u.id = (select MAX(p.id) from
    // UserEntity p)")
    // UserEntity findMaxIdUser();

    // @Query("SELECT u FROM UserEntity u WHERE u.userName = :userName AND
    // u.userEmail = :userEmail")
    // List<UserEntity> getUserEntityByTwo(@Param("userName") String userName,
    // @Param("userEmail") String userEmail);

    // // UPDATE DELETE
    // @Modifying
    // @Query("UPDATE UserEntity u SET u.userEmail = :userName")
    // @Transactional
    // int updateUserName(@Param("userName") String userName);

    // // native query
    // /**
    // * get count user use native query
    // */
    // @Query(value = "SELECT COUNT(id) FROM java_user_001", nativeQuery = true)
    // long getTotalUser();

}

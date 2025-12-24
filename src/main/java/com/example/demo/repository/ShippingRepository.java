package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.shipping.ShippingEntity;

public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {

}

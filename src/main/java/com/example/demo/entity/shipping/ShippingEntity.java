package com.example.demo.entity.shipping;

import com.example.demo.entity.order.OrderEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shipping")
public class ShippingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String receiverName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String notes;

    private String shippingStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method_id", nullable = false)
    private ShippingMethodEntity shippingMethod;
}

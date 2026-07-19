package com.example.demo.entity.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.entity.user.UserEntity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "product_id" }) // đảm bảo không trùng lặp
})
@Data

public class WishlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @CreationTimestamp
    @Column(name = "added_date", updatable = false)
    private LocalDateTime addedDate;
}

package com.example.demo;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.ProductEntity;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ProductOrderTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    @Rollback(false)
    void manyToManyInsertTest() {
        ProductEntity p1 = new ProductEntity();
        ProductEntity p2 = new ProductEntity();

        OrderEntity o1 = new OrderEntity();
        OrderEntity o2 = new OrderEntity();
        OrderEntity o3 = new OrderEntity();

        p1.setProductName("p1");
        p1.setProductPrice(new BigDecimal(4.6));

        p2.setProductName("p2");
        p2.setProductPrice(new BigDecimal(4.7));

        o1.setUserId(1);
        o2.setUserId(2);

        p1.setOrderList(List.of(o1, o2));
        p2.setOrderList(List.of(o1, o2, o3));

        orderRepository.save(o1);
        orderRepository.save(o2);
        orderRepository.save(o3);

        productRepository.save(p1);
        productRepository.save(p2);

    }
}

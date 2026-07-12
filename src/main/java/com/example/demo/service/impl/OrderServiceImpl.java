package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.req.CheckoutRequest;
import com.example.demo.dto.res.OrderDetailResponse;
import com.example.demo.dto.res.OrderResponse;
import com.example.demo.entity.order.OrderEntity;
import com.example.demo.entity.order.OrderItemEntity;
import com.example.demo.entity.payment.PaymentEntity;
import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.shipping.ShippingEntity;
import com.example.demo.entity.shipping.ShippingMethodEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;

import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ShippingMethodRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor // dùng khời tạo constructor cho thuộc tính final (@Autowired không áp dụng dc
                         // trong case final này)
public class OrderServiceImpl implements OrderService {

        @Autowired
        private PaymentRepository paymentRepository;
        @Autowired
        private ProductRepository productRepository;
        @Autowired
        private OrderRepository orderRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ShippingMethodRepository shippingMethodRepository;
        @Autowired
        private ProductService productService;
        private final OrderMapper orderMapper;
        private final OrderDetailMapper orderDetailMapper;

        @Override
        public OrderEntity processCheckout(CheckoutRequest request) {
                ShippingMethodEntity shippingMethod = shippingMethodRepository
                                .findById(request.getShipping().getShippingMethodId())
                                .orElseThrow(() -> new RuntimeException("Shipping method not found"));

                ShippingEntity shipping = new ShippingEntity();
                shipping.setReceiverName(request.getShipping().getReceiverName());
                shipping.setPhone(request.getShipping().getPhone());
                shipping.setEmail(request.getShipping().getEmail());
                shipping.setAddress(request.getShipping().getAddress());
                shipping.setCity(request.getShipping().getCity());
                shipping.setNotes(request.getShipping().getNotes());
                shipping.setShippingStatus("PENDING");
                shipping.setShippingMethod(shippingMethod);
                // snapshot fee
                shipping.setShippingFee(shippingMethod.getFee());

                UserEntity user = userRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                PaymentEntity payment = paymentRepository.findById(request.getPaymentId())
                                .orElseThrow(() -> new RuntimeException("Payment not found"));

                OrderEntity order = new OrderEntity();
                order.setUser(user);
                order.setShipping(shipping);
                order.setPayment(payment);
                order.setStatus("PENDING");
                BigDecimal subtotal = BigDecimal.ZERO;
                BigDecimal shippingFee = shippingMethod.getFee();
                int totalQuantity = 0;

                for (CheckoutRequest.CheckoutItem item : request.getItems()) {

                        ProductEntity product = productRepository.findById(item.getProductId())
                                        .orElseThrow(() -> new RuntimeException("Product not found"));

                        OrderItemEntity orderItem = new OrderItemEntity();
                        orderItem.setOrder(order);
                        orderItem.setProduct(product);
                        orderItem.setQuantity(item.getQuantity());
                        orderItem.setPrice(productService.calculateSalePrice(product)); // giá tại thời điểm mua
                        orderItem.setProductSize(item.getProductSize());
                        orderItem.setProductColor(item.getProductColor());

                        totalQuantity += item.getQuantity();
                        subtotal = subtotal.add(
                                        productService.calculateSalePrice(product)
                                                        .multiply(BigDecimal.valueOf(item.getQuantity())));

                        order.getOrderItemEntity().add(orderItem);
                }
                order.setTotalQuantity(totalQuantity);
                order.setSubtotalAmount(subtotal);
                shippingFee = shipping.getShippingFee();
                order.setTotalAmount(subtotal.add(shippingFee));

                return orderRepository.save(order); // cascade lưu OrderItem
        }

        @Override
        public List<OrderResponse> getAllOrders() {
                return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                                .stream()
                                .map(orderMapper::mapToResponse)
                                .toList();
        }

        @Override
        public OrderDetailResponse getOrderDetail(Long orderId) {
                OrderEntity order = orderRepository.findOrderDetailById(orderId)
                                .orElseThrow(() -> new RuntimeException("Order not found"));

                return orderDetailMapper.mapToResponse(order);
        }
}

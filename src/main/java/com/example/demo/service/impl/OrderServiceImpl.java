package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.req.CheckoutRequest;
import com.example.demo.dto.res.OrderDetailResponse;
import com.example.demo.dto.res.OrderItemResponse;
import com.example.demo.dto.res.OrderResponse;
import com.example.demo.dto.res.PaymentResponse;
import com.example.demo.dto.res.ShippingResponse;
import com.example.demo.entity.order.OrderEntity;
import com.example.demo.entity.order.OrderItemEntity;
import com.example.demo.entity.payment.PaymentEntity;
import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.shipping.ShippingEntity;
import com.example.demo.entity.shipping.ShippingMethodEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ShippingRepository;

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
                return orderRepository.findAll()
                                .stream()
                                .map(orderMapper::mapToResponse)
                                .toList();
        }

        @Override
        public OrderDetailResponse getOrderDetail(Long orderId) {
                OrderEntity order = orderRepository.findOrderDetailById(orderId)
                                .orElseThrow(() -> new RuntimeException("Order not found"));

                return mapToResponse(order);
        }

        private OrderDetailResponse mapToResponse(OrderEntity order) {
                OrderDetailResponse res = new OrderDetailResponse();
                res.setId(order.getId());
                res.setTotalQuantity(order.getTotalQuantity());
                res.setSubtotalAmount(order.getSubtotalAmount());
                res.setTotalAmount(order.getTotalAmount());
                res.setStatus(order.getStatus());

                // shipping
                ShippingEntity s = order.getShipping();
                ShippingResponse sr = new ShippingResponse();
                sr.setId(s.getId());
                sr.setReceiverName(s.getReceiverName());
                sr.setPhone(s.getPhone());
                sr.setEmail(s.getEmail());
                sr.setAddress(s.getAddress());
                sr.setShippingMethod(s.getShippingMethod().getName());
                res.setShipping(sr);

                // payment
                PaymentEntity p = order.getPayment();
                PaymentResponse pr = new PaymentResponse();
                pr.setId(p.getId());
                pr.setCode(p.getCode());
                pr.setName(p.getName());
                res.setPayment(pr);

                // items
                List<OrderItemResponse> items = order.getOrderItemEntity()
                                .stream()
                                .map(oi -> {
                                        OrderItemResponse ir = new OrderItemResponse();
                                        ir.setProductId(oi.getProduct().getId());
                                        ir.setProductName(oi.getProduct().getName());
                                        ir.setQuantity(oi.getQuantity());
                                        ir.setPrice(oi.getPrice());
                                        return ir;
                                })
                                .toList();

                res.setItems(items);

                return res;
        }

}

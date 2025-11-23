package com.atalaykaan.e_commerce_backend.domain.order.service;

import com.atalaykaan.e_commerce_backend.common.exception.OrderNotFoundException;
import com.atalaykaan.e_commerce_backend.domain.cart.service.CartService;
import com.atalaykaan.e_commerce_backend.domain.order.mapper.OrderMapper;
import com.atalaykaan.e_commerce_backend.domain.order.dto.request.UpdateOrderRequest;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.response.CartDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.domain.order.dto.response.OrderDTO;
import com.atalaykaan.e_commerce_backend.domain.user.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.domain.order.model.Order;
import com.atalaykaan.e_commerce_backend.domain.order.model.OrderItem;
import com.atalaykaan.e_commerce_backend.domain.order.enums.OrderStatus;
import com.atalaykaan.e_commerce_backend.domain.order.repository.OrderRepository;
import com.atalaykaan.e_commerce_backend.domain.order.kafka.KafkaProducerService;
import com.atalaykaan.e_commerce_backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderItemService orderItemService;

    private final KafkaProducerService kafkaProducerService;

    private final UserService userService;

    private final CartService cartService;

    @Transactional
    public OrderDTO placeOrder(String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        CartDTO cartDTO = cartService.findCartByUserEmail(email);

        List<CartItemDTO> cartItemDTOList = cartDTO.getCartItems();

        List<OrderItem> orderItems = cartItemDTOList
                .stream()
                .map(orderItemService::createOrderItem)
                .toList();

        LocalDateTime dateNow = LocalDateTime.now();

        Order order = Order.builder()
                .userId(userDTO.getId())
                .totalPrice(cartDTO.getTotalPrice())
                .orderItems(orderItems)
                .orderStatus(OrderStatus.RECEIVED)
                .createdAt(dateNow)
                .updatedAt(dateNow)
                .build();

        Order createdOrder = orderRepository.save(order);

        kafkaProducerService.sendOrderCreatedMessage(createdOrder);

        OrderDTO orderDTO = orderMapper.toDto(createdOrder);

        return orderDTO;
    }

    public List<OrderDTO> findOrdersByUserEmail(String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        List<OrderDTO> orderDTOList = orderRepository.findAllByUserId(userDTO.getId())
                .stream()
                .map(orderMapper::toDto)
                .toList();

        if(orderDTOList.isEmpty()) {

            throw new OrderNotFoundException("No orders were found for user with email: " + email);
        }

        return orderDTOList;
    }

    public OrderDTO findOrderById(UUID id) {

        OrderDTO orderDTO = orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        return orderDTO;
    }

    @Transactional
    public OrderDTO updateOrderById(UUID id, UpdateOrderRequest updateOrderRequest) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        order.setOrderStatus(updateOrderRequest.getOrderStatus());

        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        kafkaProducerService.sendOrderUpdatedMessage(savedOrder);

        OrderDTO orderDTO = orderMapper.toDto(savedOrder);

        return orderDTO;
    }
}

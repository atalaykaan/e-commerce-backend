package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.exception.OrderNotFoundException;
import com.atalaykaan.e_commerce_backend.mapper.OrderMapper;
import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateOrderRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.CartDTO;
import com.atalaykaan.e_commerce_backend.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.model.dto.response.OrderDTO;
import com.atalaykaan.e_commerce_backend.model.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.model.entity.Order;
import com.atalaykaan.e_commerce_backend.model.entity.OrderItem;
import com.atalaykaan.e_commerce_backend.model.enums.OrderStatus;
import com.atalaykaan.e_commerce_backend.repository.OrderRepository;
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

    private final UserService userService;

    private final CartService cartService;

    private final ProductService productService;

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

        orderItems.forEach(
                orderItem -> {
                    productService.decreaseProductStock(orderItem.getProductId(), orderItem.getQuantity());
                    orderItem.setOrder(order);
                });

        cartService.deleteCartByEmail(email);

        Order createdOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.toDto(createdOrder);

        return orderDTO;
    }

    public List<OrderDTO> findCurrentUserOrders(String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        List<OrderDTO> orderDTOList = orderRepository.findAll()
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

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.toDto(savedOrder);

        return orderDTO;
    }
}

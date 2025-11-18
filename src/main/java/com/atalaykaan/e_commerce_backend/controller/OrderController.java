package com.atalaykaan.e_commerce_backend.controller;

import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateOrderRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.OrderDTO;
import com.atalaykaan.e_commerce_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_ORDERS)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(Authentication authentication) {

        String email = authentication.getName();

        OrderDTO orderDTO = orderService.placeOrder(email);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(orderDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findCurrentUserOrders(Authentication authentication) {

        String email = authentication.getName();

        List<OrderDTO> orderDTOList = orderService.findCurrentUserOrders(email);

        return ResponseEntity.ok(orderDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrderById(@PathVariable UUID id) {

        OrderDTO orderDTO = orderService.findOrderById(id);

        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderById(@PathVariable UUID id, @Valid @RequestBody UpdateOrderRequest updateOrderRequest) {

        OrderDTO orderDTO = orderService.updateOrderById(id, updateOrderRequest);

        return ResponseEntity.ok(orderDTO);
    }
}

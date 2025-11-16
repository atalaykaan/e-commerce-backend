package com.atalaykaan.e_commerce_backend.controller;

import com.atalaykaan.e_commerce_backend.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_CART_ITEMS)
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/{id}")
    public ResponseEntity<CartItemDTO> findCartById(@PathVariable UUID id) {

        CartItemDTO cartItemDTO = cartItemService.findById(id);

        return ResponseEntity.ok(cartItemDTO);
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> findAllCartItems() {

        List<CartItemDTO> cartItemDTOList = cartItemService.findAllCarts();

        return ResponseEntity.ok(cartItemDTOList);
    }
}

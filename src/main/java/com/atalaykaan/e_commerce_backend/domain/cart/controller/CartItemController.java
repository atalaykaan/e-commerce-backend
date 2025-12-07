package com.atalaykaan.e_commerce_backend.domain.cart.controller;

import com.atalaykaan.e_commerce_backend.domain.cart.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.common.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_CART_ITEMS)
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/{id}")
    public ResponseEntity<CartItemDTO> findCartItemById(@PathVariable UUID id) {

        CartItemDTO cartItemDTO = cartItemService.findCartItemById(id);

        return ResponseEntity.ok(cartItemDTO);
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> findAllCartItems() {

        List<CartItemDTO> cartItemDTOList = cartItemService.findAllCartItems();

        return ResponseEntity.ok(cartItemDTOList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItemById(@PathVariable UUID id) {

        cartItemService.deleteCartItem(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCartItems() {

        cartItemService.deleteAllCartItems();

        return ResponseEntity.noContent().build();
    }
}

package com.atalaykaan.e_commerce_backend.domain.cart.controller;

import com.atalaykaan.e_commerce_backend.domain.cart.dto.AddItemToCartRequest;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.UpdateCartItemRequest;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.CartDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.common.constants.ApiConstants.*;


@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_CARTS)
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> addItemToCart(
            Authentication authentication,
            @Valid @RequestBody AddItemToCartRequest addItemToCartRequest) {

        String email = authentication.getName();

        CartDTO cartDTO = cartService.addItemToCart(email, addItemToCartRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cartDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(cartDTO);
    }

    @GetMapping
    public ResponseEntity<CartDTO> findCurrentUserCart(Authentication authentication) {

        String email = authentication.getName();

        CartDTO cartDTO = cartService.findCartByUserEmail(email);

        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> findCartById(@PathVariable UUID id) {

        CartDTO cartDTO = cartService.findCartById(id);

        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<CartDTO> findCartByUserId(@PathVariable UUID userId) {

        CartDTO cartDTO = cartService.findCartByUserId(userId);

        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CartDTO>> findAllCarts() {

        List<CartDTO> cartDTOList = cartService.findAllCarts();

        return ResponseEntity.ok(cartDTOList);
    }

    @PutMapping
    public ResponseEntity<CartDTO> updateItemQuantityInCart(
            Authentication authentication,
            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {

        String email = authentication.getName();

        CartDTO cartDTO = cartService.updateCartItemQuantityInCart(email, updateCartItemRequest);

        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity<CartDTO> removeItemFromCart(
            Authentication authentication,
            @PathVariable UUID cartItemId) {

        String email = authentication.getName();

        CartDTO cartDTO = cartService.removeItemFromCart(email, cartItemId);

        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUserCart(Authentication authentication) {

        String email = authentication.getName();

        cartService.deleteCartByEmail(email);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteCartById(@PathVariable UUID id) {

        cartService.deleteCartById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllCarts() {

        cartService.deleteAllCarts();

        return ResponseEntity.noContent().build();
    }
}

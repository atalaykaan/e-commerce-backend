package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.exception.CartItemNotFoundException;
import com.atalaykaan.e_commerce_backend.exception.InvalidItemQuantityException;
import com.atalaykaan.e_commerce_backend.mapper.CartItemMapper;
import com.atalaykaan.e_commerce_backend.model.dto.request.create.AddItemToCartRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.model.entity.CartItem;
import com.atalaykaan.e_commerce_backend.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartItemMapper;

    protected CartItem createCartItem(AddItemToCartRequest addItemToCartRequest) {

        LocalDateTime dateNow = LocalDateTime.now();

        return CartItem.builder()
                .productId(addItemToCartRequest.getProductId())
                .quantity(addItemToCartRequest.getQuantity())
                .createdAt(dateNow)
                .updatedAt(dateNow)
                .build();
    }

    public CartItemDTO findById(UUID id) {

        CartItem cartItem = getById(id);

        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        return cartItemDTO;
    }

    public List<CartItemDTO> findAllCarts() {

        List<CartItemDTO> cartItemDTOList = cartItemRepository.findAll()
                .stream()
                .map(cartItemMapper::toDto)
                .toList();

        if(cartItemDTOList.isEmpty()) {

            throw new CartItemNotFoundException("No cart items were found");
        }

        return cartItemDTOList;
    }

    protected CartItem getById(UUID id) {

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + id));

        return cartItem;
    }

    protected void updateCartItemQuantity(CartItem cartItem, int quantity) {

        if(quantity < 0) {

            throw new InvalidItemQuantityException("Item quantity cannot be less than zero");
        }

        cartItem.setQuantity(quantity);
    }

    @Transactional
    protected void deleteCartItem(UUID id) {

        getById(id);

        cartItemRepository.deleteById(id);
    }
}

package com.atalaykaan.e_commerce_backend.domain.cart.service;

import com.atalaykaan.e_commerce_backend.common.exception.CartItemNotFoundException;
import com.atalaykaan.e_commerce_backend.common.exception.InvalidItemQuantityException;
import com.atalaykaan.e_commerce_backend.domain.cart.mapper.CartItemMapper;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.AddItemToCartRequest;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.CartItemDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.model.CartItem;
import com.atalaykaan.e_commerce_backend.domain.cart.repository.CartItemRepository;
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

    public CartItemDTO findCartItemById(UUID id) {

        CartItem cartItem = getCartItemById(id);

        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        return cartItemDTO;
    }

    public List<CartItemDTO> findAllCartItems() {

        List<CartItemDTO> cartItemDTOList = cartItemRepository.findAll()
                .stream()
                .map(cartItemMapper::toDto)
                .toList();

        if(cartItemDTOList.isEmpty()) {

            throw new CartItemNotFoundException("No cart items were found");
        }

        return cartItemDTOList;
    }

    protected CartItem getCartItemById(UUID id) {

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + id));

        return cartItem;
    }

    protected void updateCartItemQuantity(CartItem cartItem, Long quantity) {

        if(quantity < 0) {

            throw new InvalidItemQuantityException("Item quantity cannot be less than zero");
        }

        cartItem.setQuantity(quantity);
    }

    @Transactional
    public void deleteCartItem(UUID id) {

        getCartItemById(id);

        cartItemRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllCartItems() {

        List<CartItem> cartItemList = cartItemRepository.findAll();

        if(cartItemList.isEmpty()) {

            throw new CartItemNotFoundException("No cart items were found");
        }

        cartItemRepository.deleteAll();
    }
}

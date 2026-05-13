package com.ecommerce.project.service;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> getAllCarts();
    CartDTO getCartById(Long cartId);
}
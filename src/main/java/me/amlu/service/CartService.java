package me.amlu.service;

import me.amlu.model.Cart;
import me.amlu.model.CartItem;
import me.amlu.request.AddCartItemRequest;

import java.math.BigDecimal;

public interface CartService {

    CartItem addItemToCart(AddCartItemRequest request, String token) throws Exception;

    CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

    Cart removeCartItem(Long cartItemId, String token) throws Exception;

    BigDecimal calculateTotalAmount(Cart cart) throws Exception;

    Cart findCartById(Long cartId) throws Exception;

    Cart findCartByCustomerId(Long customerId) throws Exception;
    

    Cart clearCart(Long customerId) throws Exception;



}

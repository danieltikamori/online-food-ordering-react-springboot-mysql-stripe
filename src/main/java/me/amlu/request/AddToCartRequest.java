package me.amlu.request;

import lombok.Data;
import me.amlu.model.CartItem;
@Data
public class AddToCartRequest {

    private String token;
    private CartItem cartItem;

}
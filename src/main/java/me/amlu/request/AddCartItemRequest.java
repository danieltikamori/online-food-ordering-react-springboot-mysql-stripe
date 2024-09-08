package me.amlu.request;

import lombok.Data;
import me.amlu.dto.TokenObject;

import java.util.List;

@Data
public class AddCartItemRequest {

//    private String token;
//    private CartItem cartItem;
    private TokenObject token;
    private Long foodId;
    private int quantity;

    private List<String> ingredients;

//    private List<IngredientItemDto> ingredients;
}

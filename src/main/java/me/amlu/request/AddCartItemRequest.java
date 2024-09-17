/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import me.amlu.dto.TokenObject;
import me.amlu.model.IngredientsItems;

import java.util.Set;

/*
 @NotNull: a constrained CharSequence, Collection, Map, or Array is valid as long as it’s not null, but it can be empty.
 @NotEmpty: a constrained CharSequence, Collection, Map, or Array is valid as long as it’s not null, and its size/length is greater than zero.
 @NotEmpty: a constrained String is valid as long as it’s not null, and the trimmed length is greater than zero.
*/

@Data
public class AddCartItemRequest {

//    private String token;
//    private CartItem cartItem;
    @NotEmpty(message = "Token cannot be blank.")
    private TokenObject token;

    @Positive(message = "FoodId must be a positive number.")
    private Long foodId;

    @Max(value = 63, message = "Quantity cannot be greater than 63.")
    @Positive(message = "Quantity must be a positive number.")
    private int quantity;

    // Real database entity
    private Set<IngredientsItems> ingredients;

}

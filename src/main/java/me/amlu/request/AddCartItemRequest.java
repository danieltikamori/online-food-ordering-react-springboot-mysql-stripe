/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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

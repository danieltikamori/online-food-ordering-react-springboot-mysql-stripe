/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import me.amlu.model.Food;
import me.amlu.model.IngredientsItems;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartItemDto {

    @NotEmpty
    Food food;

    @Positive
    private Long foodId;

    @NotEmpty
    private String foodName;

    @PositiveOrZero
    private BigDecimal foodPrice;

    @Positive
    private int quantity;

    @NotEmpty
    private String idempotencyKey;

    private Set<IngredientsItems> ingredients;

    private Set<Long> ingredientsIds;

}


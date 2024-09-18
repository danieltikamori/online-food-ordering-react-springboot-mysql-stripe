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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import me.amlu.model.Category;
import me.amlu.model.IngredientsItems;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class CreateFoodRequest {

    @Size(max = 255)
    @NotEmpty(message = "Name cannot be blank.")
    private String name;

    @Size(max = 2047)
    private String description;

    @Positive(message = "Price must be a positive number.")
    private BigDecimal price;

    @NotEmpty(message = "Category cannot be empty.")
    private Category category;

    @NotEmpty(message = "Images cannot be empty.")
    private List<String> images;

    @Positive
    private Long restaurantId;

    private boolean isVegetarian;
    private boolean isSeasonal;

    private Set<IngredientsItems> ingredients;

}

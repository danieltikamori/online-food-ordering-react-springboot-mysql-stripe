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
import lombok.Data;
import me.amlu.model.Category;
import me.amlu.model.IngredientsItems;
import me.amlu.model.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class UpdateFoodRequest {

    @NotEmpty(message = "Name cannot be blank.")
    private String name;

    private String description;

    @NotEmpty(message = "Category cannot be empty.")
    private Category foodCategory;

    @Positive(message = "Price must be a positive number.")
    private BigDecimal price;

    private List<String> images;

    private boolean isAvailable;
    private boolean isVegetarian;
    private boolean isSeasonal;

    private Set<IngredientsItems> ingredients;

    @NotEmpty
    private Instant updatedAt;

    @NotEmpty
    private User updatedBy;

}

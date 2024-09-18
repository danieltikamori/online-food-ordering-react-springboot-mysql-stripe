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

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import me.amlu.model.IngredientCategory;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Data
@Embeddable
public class IngredientItemDto {

    @Column(insertable = false, updatable = false) // Ignore this category_id for CartItemIngredient mapping
    private Long id;

    @Column(nullable = false, length = 127)
    @NotNull
    @NotEmpty(message = "Ingredient name cannot be blank.")
    @Size(max = 127)
    private String ingredientName;

    @ManyToOne
    private IngredientCategory ingredientCategory;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    private boolean inStock;

    @PreRemove
    private void preRemove() {
        this.deletedAt = Instant.now();
        this.deletedBy = getAuthenticatedUser();
    }

    @CreatedDate
    private Instant createdAt;

    @CreatedBy
    @ManyToOne
    private User createdBy;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    @ManyToOne
    private User updatedBy;

    private Instant deletedAt;

    @ManyToOne
    private User deletedBy;

}

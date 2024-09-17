/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import me.amlu.model.IngredientCategory;
import me.amlu.model.Restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

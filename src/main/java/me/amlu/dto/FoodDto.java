/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import me.amlu.model.Category;
import me.amlu.model.Restaurant;

import me.amlu.model.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Data
@Embeddable
public class FoodDto {
    private Long food_id;

    @Column(nullable = false, length = 255)
    @NotNull
    @NotEmpty(message = "Name cannot be blank.")
    @Size(max = 255)
    private String name;

    @Column(length = 2047)
    @Size(max = 2047)
    private String description;

    @ManyToOne
    private Category foodCategory;

    @Column(nullable = false)
    @PositiveOrZero(message = "Price must be a positive value.")
    private BigDecimal price;

    @Column(length = 8191)
    @Size(max = 8191)
    @ElementCollection
    private List<String> images;

    private boolean isAvailable;

    @ManyToOne
    private Restaurant restaurant;

    private boolean isVegetarian;
    private boolean isSeasonal;

    @ManyToMany
    @Size(max = 8191)
    private Set<IngredientItemDto> ingredients = Collections.synchronizedSet(new LinkedHashSet<>());

    @PreRemove
    private void preRemove() {
        this.deletedAt = Instant.now();
        this.deletedBy = getAuthenticatedUser();
    }

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;
    private Instant deletedAt;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User deletedBy;
}

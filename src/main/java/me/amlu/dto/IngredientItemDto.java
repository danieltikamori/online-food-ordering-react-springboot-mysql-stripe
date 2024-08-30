package me.amlu.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import me.amlu.model.IngredientCategory;
import me.amlu.model.Restaurant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Embeddable
public class IngredientItemDto {

    @Column(insertable = false, updatable = false) // Ignore this id for CartItemIngredient mapping
    private Long id;

    @Column(nullable = false, length = 127)
    @NotNull
    @NotBlank(message = "Ingredient name cannot be blank.")
    @Size(max = 127)
    private String ingredientName;

    @ManyToOne
    private IngredientCategory ingredientCategory;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    private boolean inStock;
}

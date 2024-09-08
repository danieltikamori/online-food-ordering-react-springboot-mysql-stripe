package me.amlu.dto;

import jakarta.persistence.*;
import lombok.Data;
import me.amlu.model.Category;
import me.amlu.model.IngredientsItems;
import me.amlu.model.Restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Embeddable
public class FoodDto {
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 255)
    private String name;

    @Column(length = 2047)
    @Size(max = 2047)
    private String description;

    @ManyToOne
    private Category foodCategory;

    @Column(nullable = false)
    @NotNull
    @NotBlank(message = "Price cannot be blank.")
    private BigDecimal price;

    @Column(length = 8191)
    @Size(max = 8191)
    @ElementCollection
    private List<String> images;

    private boolean available;

    @ManyToOne
    private Restaurant restaurant;

    private boolean isVegetarian;
    private boolean isSeasonal;

    @ManyToMany
    @Size(max = 8191)
    private List<IngredientItemDto> ingredients = new ArrayList<>();

    private Date creationDate;
    private Date updateDate;
}

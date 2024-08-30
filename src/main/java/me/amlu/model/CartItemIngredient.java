package me.amlu.model;

import jakarta.persistence.*;
import lombok.*;
import me.amlu.dto.IngredientItemDto;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

@Cacheable(true) @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class CartItemIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private CartItem cartItem;

    @Embedded // Or use appropriate mapping for IngredientItemDto
    private IngredientItemDto ingredient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemIngredient that = (CartItemIngredient) o;
        return Objects.equals(cartItem, that.cartItem) && Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItem, ingredient);
    }
}
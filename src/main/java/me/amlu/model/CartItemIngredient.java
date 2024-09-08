package me.amlu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.amlu.dto.IngredientItemDto;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.Instant;
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

    @Column(nullable = false, name = "created_at", updatable = false, columnDefinition = "DATETIME ZONE='UTC'")
    @NotNull
    @NotBlank
    private Instant createdAt;

    @Column(nullable = false, name = "created_by", updatable = false)
    @NotNull
    @NotBlank
    private User createdBy;

    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME ZONE='UTC'")
    @NotNull
    @NotBlank
    private Instant updatedAt;

    @Column(nullable = false, name = "updated_by")
    @NotNull
    @NotBlank
    private User updatedBy;

    @Column(nullable = true, name = "deleted_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant deletedAt;

    @ManyToOne
    @JoinColumn(nullable = true, name = "deleted_by_id")
    private User deletedBy;

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
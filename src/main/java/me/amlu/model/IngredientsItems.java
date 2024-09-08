package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.proxy.HibernateProxy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Objects;

@Entity
@Cacheable(true) @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class IngredientsItems {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    private boolean inStock = true;

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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        IngredientsItems that = (IngredientsItems) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

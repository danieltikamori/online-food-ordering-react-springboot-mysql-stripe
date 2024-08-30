package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.proxy.HibernateProxy;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Cacheable(true) @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
//@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class IngredientCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 63)
    @NotNull
    @NotBlank(message = "Category name cannot be blank.")
    @Size(max = 63)
    private String categoryName;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    @JsonIgnore
    @Column(nullable = false, length = 8191)
    @NotNull
    @NotBlank(message = "Ingredients cannot be blank.")
    @Size(max = 8191)
    @OneToMany(mappedBy = "ingredientCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientsItems> ingredients = new ArrayList<>();


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        IngredientCategory that = (IngredientCategory) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

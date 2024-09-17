/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import me.amlu.config.SensitiveData;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;
import org.joou.UInteger;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@FilterDef(name = "adminFilter", defaultCondition = "1=1")
@Table(indexes = @Index(name = "cart_deleted_at_index", columnList = "cart_id, deleted_at"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "idempotency_key"}))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cart_item_id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned integer DEFAULT 0", nullable = false)
    private UInteger version = UInteger.valueOf(0);

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @JsonIgnore
    @ManyToOne
    @Filter(name = "deletedFilter")
    private Cart cart;

    @ManyToOne
    @Filter(name = "deletedFilter")
    @JoinColumn(name = "food_id")
    private Food food;

    @Min(1)
    @Max(63)
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    @Size(max = 63)
    private int quantity;

    @Max(63)
    @Size(max = 63)
    @Filter(name = "deletedFilter")
    @Column(nullable = true)
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "cart_item_ingredient",
            joinColumns = @JoinColumn(name = "cart_item_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @ToString.Exclude
    private Set<IngredientsItems> ingredients = Collections.synchronizedSet(new LinkedHashSet<>());

    @PreRemove
    private void preRemove() {
        this.deletedAt = Instant.now();
        this.deletedBy = getAuthenticatedUser();
    }

    @CreatedDate
    @NotNull
    @NotEmpty
    @Column(nullable = false, name = "created_at", updatable = false, columnDefinition = "DATETIME ZONE='UTC'")
    private Instant createdAt;

    @CreatedBy
    @NotNull
    @NotEmpty
    @ManyToOne
    @JoinColumn(nullable = false, name = "created_by_id", updatable = false)
    private User createdBy;

    @LastModifiedDate
    @NotNull
    @NotEmpty
    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant updatedAt;

    @LastModifiedBy
    @NotNull
    @NotEmpty
    @ManyToOne
    @JoinColumn(nullable = false, name = "updated_by_id")
    private User updatedBy;

    @SoftDelete
    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "deleted_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant deletedAt;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @ManyToOne
    @JoinColumn(nullable = true, name = "deleted_by_id")
    private User deletedBy;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return getCart_item_id() != null && Objects.equals(getCart_item_id(), cartItem.getCart_item_id());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

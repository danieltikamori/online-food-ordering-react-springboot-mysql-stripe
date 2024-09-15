package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@Filter(name = "deletedFilter")
@Table(name = "orders", indexes = @Index(name = "customer_deleted_at_index", columnList = "customer_id, deleted_at"), uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id"}))
@Cacheable(true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned integer DEFAULT 0", nullable = false)
    private Integer version = 0;

    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Size(max = 63)
    private ORDER_STATUS orderStatus;

    @PreRemove
    private void preRemove() {
        this.deletedAt = Instant.now();
        this.deletedBy = getAuthenticatedUser();
    }

    @CreatedDate
    @NotNull
    @NotBlank
    @Column(nullable = false, name = "created_at", updatable = false, columnDefinition = "DATETIME ZONE='UTC'")
    private Instant createdAt;

    @CreatedBy
    @NotNull
    @NotBlank
    @Column(nullable = false, name = "created_by", updatable = false)
    private User createdBy;

    @LastModifiedDate
    @NotNull
    @NotBlank
    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant updatedAt;

    @LastModifiedBy
    @NotNull
    @NotBlank
    @Column(nullable = false, name = "updated_by")
    private User updatedBy;

    @SoftDelete
    @Column(nullable = true, name = "deleted_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant deletedAt;

    @ManyToOne
    @JoinColumn(nullable = true, name = "deleted_by_id")
    private User deletedBy;

    @ManyToOne
    private Address deliveryAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> orderItems;

//    private Payment payment;

    @Min(1)
    @Max(8191)
    @Column(nullable = false)
    @NotNull
    @NotBlank
    @Size(max = 8191)
    private int totalItems;

    private BigDecimal totalAmount;

    public boolean canTransitionTo(ORDER_STATUS newStatus) {
        return switch (this.orderStatus) {
            case PENDING -> newStatus == ORDER_STATUS.CANCELLED
                    || newStatus == ORDER_STATUS.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> newStatus == ORDER_STATUS.DELIVERED;
            case DELIVERED -> newStatus == ORDER_STATUS.COMPLETED;
            default -> false;
        };
    }

    public boolean cancel() {
        if (this.orderStatus == ORDER_STATUS.PENDING) {
            this.orderStatus = ORDER_STATUS.CANCELLED;
            this.setUpdatedAt(Instant.now());
            this.setUpdatedBy(getAuthenticatedUser());
            return true; // Cancellation successful
        }
        return false; // Cancellation not allowed in the current state
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

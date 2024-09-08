package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Entity
@Cacheable(true) @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Size(max = 63)
    private ORDER_STATUS orderStatus;

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

    @ManyToOne
    private Address deliveryAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> orderItems;

//    private Payment payment;

    @Max(8191)
    @Column(nullable = false)
    @NotNull
    @NotBlank
    @Size(max = 8191)
    private int totalItems;

    private BigDecimal totalAmount;

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

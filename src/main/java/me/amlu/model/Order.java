/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import me.amlu.config.SensitiveData;
import me.amlu.repository.OrderRepository;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;
import org.joou.UInteger;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@FilterDef(name = "adminFilter", defaultCondition = "1=1")
@Table(name = "orders", indexes = @Index(name = "customer_deleted_at_index", columnList = "customer_id, deleted_at"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "idempotency_key"}))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Order extends AnonymizedData {

    @Transient
    private final OrderRepository orderRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long order_id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned integer DEFAULT 0", nullable = false)
    private UInteger version = UInteger.valueOf(0);

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Size(max = 63)
    private OrderStatus orderStatus;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private CopyOnWriteArrayList<OrderItem> orderItems;

//    private Payment payment;

    @Min(1)
    @Max(8191)
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    @Size(max = 8191)
    @Positive(message = "Total items must be a positive number.")
    private int totalItems;

    @Min(0)
    @Max(8191)
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    @Size(max = 8191)
    @PositiveOrZero(message = "Total amount must be a positive number.")
    private BigDecimal totalAmount;

    public Order(OrderRepository orderRepository) {
        super();
        this.orderRepository = orderRepository;
        this.orderItems = new CopyOnWriteArrayList<>();
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this.orderStatus) {
            case PENDING -> newStatus == OrderStatus.CANCELLED
                    || newStatus == OrderStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> newStatus == OrderStatus.DELIVERED;
            case DELIVERED -> newStatus == OrderStatus.COMPLETED;
            default -> false;
        };
    }

    public boolean cancel() {
        if (this.orderStatus == OrderStatus.PENDING) {
            this.orderStatus = OrderStatus.CANCELLED;
            this.setUpdatedAt(Instant.now());
            this.setUpdatedBy(getAuthenticatedUser());
            return true; // Cancellation successful
        }
        return false; // Cancellation not allowed in the current state
    }

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

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "order_data_exported_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant orderDataExportedAt;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "is_order_data_exported")
    private boolean isOrderDataExported;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "order_data_exported_key")
    private String orderDataExportedKey;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getOrder_id() != null && Objects.equals(getOrder_id(), order.getOrder_id());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public void delete() {
        orderRepository.deleteById(this.getId());
    }
}

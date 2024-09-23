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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import me.amlu.config.SensitiveData;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.proxy.HibernateProxy;
import org.joou.UInteger;
import org.joou.ULong;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@FilterDef(name = "adminFilter", defaultCondition = "1=1")
@Table(indexes = @Index(name = "customer_deleted_at_index", columnList = "customer_id, deleted_at"), uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "idempotency_key"}))
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cart_id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned bigint DEFAULT 0", nullable = false)
    private ULong version = ULong.valueOf(0);

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @OneToOne
    private User customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Filter(name = "deletedFilter")
    private CopyOnWriteArrayList<CartItem> cartItems = new CopyOnWriteArrayList<>(); //<Food, Quantity>

    @PreRemove
    private void preRemove() {
        this.deletedAt = Instant.now();
        this.deletedBy = getAuthenticatedUser();
    }

    @CreatedDate
    @NotEmpty
    @Column(nullable = false, name = "created_at", updatable = false, columnDefinition = "DATETIME ZONE='UTC'")
    private Instant createdAt;

    @CreatedBy
    @NotEmpty
    @ManyToOne
    @JoinColumn(nullable = false, name = "created_by_id", updatable = false)
    private User createdBy;

    @LastModifiedDate
    @NotEmpty
    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant updatedAt;

    @LastModifiedBy
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
        Cart cart = (Cart) o;
        return getCart_id() != null && Objects.equals(getCart_id(), cart.getCart_id());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

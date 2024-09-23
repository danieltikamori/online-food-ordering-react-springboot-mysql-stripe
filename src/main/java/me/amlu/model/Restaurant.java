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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.amlu.config.SensitiveData;
import me.amlu.repository.RestaurantRepository;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.constraints.URL;
import org.joou.ULong;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@FilterDef(name = "adminFilter", defaultCondition = "1=1")
@Table(indexes = @Index(name = "cuisine_deleted_at_index", columnList = "cuisine_type, deleted_at"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "restaurant_name", "idempotency_key"}))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Restaurant extends AnonymizedData {

    @Transient
    private final RestaurantRepository restaurantRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long restaurant_id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned bigint DEFAULT 0", nullable = false)
    private ULong version = ULong.valueOf(0L);

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @OneToOne
    private User owner;

    @OneToMany
    @JsonIgnore
    @ToString.Exclude
    private CopyOnWriteArrayList<User> restaurantStaff = new CopyOnWriteArrayList<>();

    @Column(nullable = false)
    @NotEmpty(message = "Restaurant name cannot be blank.")
    @Size(max = 255)
    private String restaurantName;

    @Column(length = 2047)
    @Size(max = 2047)
    private String description;

    @Column(length = 63)
    @Size(max = 63)
    private String cuisineType;

    @NotEmpty
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Originally OneToOne
    private Address address;

    @NotEmpty
    @Embedded
    private ContactInformation contactInformation;

    @NotEmpty
    @Column(nullable = false)
    @Size(max = 255)
    private String openingHours;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private CopyOnWriteArrayList<Order> orders = new CopyOnWriteArrayList<>();

    @URL
    @NotEmpty
    @Size(max = 8191)
    @ElementCollection
    @Column(length = 8191)
    @Size(max = 8191)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private CopyOnWriteArrayList<String> imagesURL = new CopyOnWriteArrayList<>();

    @Column(nullable = false)
    @NotEmpty
    private boolean openNow = true;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private CopyOnWriteArrayList<Food> foods = new CopyOnWriteArrayList<>();

    public Restaurant(RestaurantRepository restaurantRepository, RestaurantRepository restaurantRepository1) {
        super();
        this.restaurantRepository = restaurantRepository1;
    }

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

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "restaurant_data_exported_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant restaurantDataExportedAt;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = false, name = "is_restaurant_data_exported")
    private boolean isRestaurantDataExported;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "restaurant_data_exported_key")
    private String restaurantDataExportedKey;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "anonymized_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant anonymizedAt;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @ManyToOne
    @JoinColumn(nullable = true, name = "anonymized_by_id")
    private User anonymizedBy;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "anonymized_data_exported_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant anonymizedDataExportedAt;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @ManyToOne
    @JoinColumn(nullable = false, name = "is_anonymized_data_exported")
    private boolean isAnonymizedDataExported;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "anonymized_data_exported_key")
    private String AnonymizedDataExportedKey;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Restaurant that = (Restaurant) o;
        return getRestaurant_id() != null && Objects.equals(getRestaurant_id(), that.getRestaurant_id());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    @Async
    @Transactional
    public void delete() {
        assert restaurantRepository != null;
        restaurantRepository.deleteById(this.getId());
    }

}

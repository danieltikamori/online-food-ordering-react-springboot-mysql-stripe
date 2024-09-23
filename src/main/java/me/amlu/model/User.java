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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.amlu.config.SensitiveData;
import me.amlu.config.StrongPassword;
import me.amlu.dto.RestaurantDto;
import me.amlu.repository.UserRepository;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;
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

@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@FilterDef(name = "adminFilter", defaultCondition = "1=1")
@Table(indexes = @Index(name = "email_deleted_at_index", columnList = "email, deleted_at"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"email", "idempotency_key"}))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User extends AnonymizedData {
    @Transient
    private final UserRepository userRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned bigint DEFAULT 0", nullable = false)
    private ULong version = ULong.valueOf(0);


    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    @NotEmpty
    @Size(max = 255)
    private String fullName;

    @Email
    @Column(nullable = false, unique = true)
    @NotEmpty
    @Size(max = 255)
    private String email;

    // To avoid password leakage, we will use the JsonProperty annotation as write-only.
    @StrongPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotEmpty
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @SensitiveData(rolesAllowed = {"ADMIN", "CUSTOMER"})
    @Column(nullable = false)
    private Phonenumber.PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotEmpty
    private UserRole role = UserRole.ROLE_CUSTOMER;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    private CopyOnWriteArrayList<Order> orders = new CopyOnWriteArrayList<>();

    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private CopyOnWriteArrayList<RestaurantDto> favoriteRestaurants = new CopyOnWriteArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Column(length = 8191)
    private CopyOnWriteArrayList<Address> addresses = new CopyOnWriteArrayList<>();

    public User(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
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
    @Column(nullable = true, name = "user_data_exported_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant userDataExportedAt;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = false, name = "is_user_data_exported")
    private boolean isUserDataExported;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @Column(nullable = true, name = "user_data_exported_key")
    private String userDataExportedKey;

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
    private String anonymizedDataExportedKey;


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
        User user = (User) o;
        return getUser_id() != null && Objects.equals(getUser_id(), user.getUser_id());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    @Async
    @Transactional
    public void delete() {
        userRepository.deleteById(this.getId());
    }
}

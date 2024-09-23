/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.amlu.model.Address;
import me.amlu.model.Order;
import me.amlu.model.UserRole;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.joou.ULong;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.concurrent.CopyOnWriteArrayList;

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
@RequiredArgsConstructor
@AllArgsConstructor
public class UserToOrderDto {

    @Id
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotEmpty
    private UserRole role = UserRole.ROLE_CUSTOMER;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    private CopyOnWriteArrayList<Order> orders = new CopyOnWriteArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Column(length = 8191)
    private CopyOnWriteArrayList<Address> addresses = new CopyOnWriteArrayList<>();

    @LastModifiedDate
    @NotEmpty
    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant updatedAt;

    @LastModifiedBy
    @NotEmpty
    @ManyToOne
    @JoinColumn(nullable = false, name = "updated_by_id")
    private UserToOrderDto updatedBy;

}

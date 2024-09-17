/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.amlu.config.SensitiveData;
import me.amlu.config.ValidPostalCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.joou.UInteger;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@FilterDef(name = "adminFilter", defaultCondition = "1=1")
@Table(indexes = @Index(name = "address_deleted_at_index", columnList = "street_address, deleted_at"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"idempotency_key"}))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long address_id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned integer DEFAULT 0", nullable = false)
    private UInteger version = UInteger.valueOf(0);

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @PreRemove
    private void preRemove() {
        this.deletedAt = Instant.now();
        this.deletedBy = getAuthenticatedUser();
    }

    @Filter(name = "deletedFilter")
    @Column(nullable = false)
    @NotEmpty(message = "Street address cannot be blank.")
    @Size(max = 255)
    private String streetAddress;

    @Column(nullable = false)
    @NotEmpty(message = "City cannot be blank.")
    @Size(max = 127)
    private String city;

    @Column(nullable = false)
    @NotEmpty(message = "State/Province cannot be blank.")
    @Size(max = 127)
    private String stateProvince;

    @ValidPostalCode
    @Column(nullable = false)
    @NotEmpty(message = "Postal code cannot be blank.")
    @Size(max = 31)
    private String postalCode;

    @Column(nullable = false)
    @NotEmpty(message = "Country cannot be blank.")
    @Size(min = 2, max = 2, message = "Country code must be 2 characters")
    private String country;

    @CreatedDate
    @NotEmpty
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME ZONE='UTC'")
    private Instant createdAt;

    @CreatedBy
    @NotEmpty
    @JoinColumn(nullable = false, name = "created_by_id", updatable = false)
    @ManyToOne
    private User createdBy;

    @LastModifiedDate
    @NotEmpty
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME ZONE='UTC'")
    private Instant updatedAt;

    @LastModifiedBy
    @NotEmpty
    @JoinColumn(nullable = false, name = "updated_by_id")
    @ManyToOne
    private User updatedBy;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @SoftDelete
    @Column(nullable = true, name = "deleted_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant deletedAt;

    @SensitiveData(rolesAllowed = {"ADMIN", "ROOT"})
    @ManyToOne
    @JoinColumn(nullable = true, name = "deleted_by_id")
    private User deletedBy;
}

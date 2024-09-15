package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@Filter(name = "deletedFilter")
@Table(indexes = @Index(name = "cuisine_deleted_at_index", columnList = "cuisine_type, deleted_at"), uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id"}))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned bigint DEFAULT 0", nullable = false)
    private Long version = 0L;

    @OneToOne
    private User owner;

    @Column(nullable = false)
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String restaurantName;

    @Column(length = 2047)
    @Size(max = 2047)
    private String description;

    @Column(length = 63)
    @Size(max = 63)
    private String cuisineType;

    @OneToOne
    private Address address;

    @Embedded
    private ContactInformation contactInformation;

    @Column(nullable = true)
    @Size(max = 255)
    private String openingHours;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @ElementCollection
    @Column(length = 8191)
    @Size(max = 8191)
    private List<String> images;

    private boolean openNow = true;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Food> foods = new ArrayList<>();

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

    @Column(nullable = true, name = "anonymized_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant anonymizedAt;

    @ManyToOne
    @JoinColumn(nullable = true, name = "anonymized_by_id")
    private User anonymizedBy;

    @Column(nullable = true, name = "anonymized_data_exported_at", columnDefinition = "DATETIME ZONE='UTC'")
    private Instant anonymizedDataExportedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "is_anonymized_data_exported")
    private boolean isAnonymizedDataExported;


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
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}

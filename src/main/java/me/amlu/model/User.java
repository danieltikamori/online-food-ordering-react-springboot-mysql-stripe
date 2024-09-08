package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.amlu.dto.RestaurantDto;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Cacheable(true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String fullName;

    @Column(nullable = false, unique = true, length = 255)
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String email;


    // To avoid password leakage, we will use the JsonProperty annotation as write-only.
//    @StrongPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotNull
    @NotBlank
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    @NotBlank
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @ElementCollection
    private List<RestaurantDto> favoriteRestaurants = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Column(length = 8191)
    private List<Address> addresses = new ArrayList<>();

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
    private String anonymizedDataExportedKey;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}

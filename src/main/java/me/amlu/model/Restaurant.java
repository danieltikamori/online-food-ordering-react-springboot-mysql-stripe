package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.proxy.HibernateProxy;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Cacheable(true) @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User owner;

    @Column(nullable = false, length = 255)
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

    @Column(length = 255)
    @Size(max = 255)
    private String openingHours;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @ElementCollection
    @Column(length = 8191)
    @Size(max = 8191)
    private List<String> images;

    private LocalDateTime registrationDate;

    private LocalDateTime updateDate;

    private boolean openNow = true;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Food> foods = new ArrayList<>();

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

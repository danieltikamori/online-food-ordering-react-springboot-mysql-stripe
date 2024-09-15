package me.amlu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SoftDelete;
import org.joou.UInteger;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SoftDelete
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@Table(indexes = @Index(name = "address_deleted_at_index", columnList = "street_address, deleted_at"))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "u_lmod", columnDefinition = "unsigned integer DEFAULT 0", nullable = false)
    private UInteger version = UInteger.valueOf(0);

    @Filter(name = "deletedFilter")
    @Column(nullable = false)
    @NotNull
    @NotBlank(message = "Street address cannot be blank.")
    @Size(max = 255)
    private String streetAddress;


    @Column(nullable = false)
    @NotNull
    @NotBlank(message = "City cannot be blank.")
    @Size(max = 127)
    private String city;

    @Column(nullable = false)
    @NotNull
    @NotBlank(message = "State/Province cannot be blank.")
    @Size(max = 127)
    private String stateProvince;

    @Column(nullable = false)
    @NotNull
    @NotBlank(message = "Postal code cannot be blank.")
    @Size(max = 31)
    private String postalCode;

    @Column(nullable = false)
    @NotNull
    @NotBlank(message = "Country cannot be blank.")
    @Size(max = 127)
    private String country;

}

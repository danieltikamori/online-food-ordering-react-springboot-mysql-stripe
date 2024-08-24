package me.amlu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Cacheable(true) @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank(message = "Street address cannot be blank.")
    @Size(max = 255)
    private String streetAddress;


    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank(message = "City cannot be blank.")
    @Size(max = 255)
    private String city;

    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank(message = "State/Province cannot be blank.")
    @Size(max = 255)
    private String stateProvince;

    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank(message = "Postal code cannot be blank.")
    @Size(max = 255)
    private String postalCode;

    @Column(nullable = false, length = 255)
    @NotNull
    @NotBlank(message = "Country cannot be blank.")
    @Size(max = 255)
    private String country;

}

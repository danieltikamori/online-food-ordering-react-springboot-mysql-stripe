package me.amlu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User owner;

    private String restaurantName;

    private String description;
    private String cuisineType;

    @ManyToOne
    private Address address;

    private String openingTime;
    private String closingTime;

    @Embedded
    private ContactInformation contactInformation;

    private String openingHours;

    private String closingHours;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @ElementCollection
    @Column(length = 1000)
    private List<String> images;

    private LocalDateTime registrationDate;

    private LocalDateTime updateDate;

    private boolean openNow = true;


    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>();



}

package me.amlu.model;

import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.amlu.dto.RestaurantDto;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AnonymizedData {

    User user;
    private String name;
    private String email;
    private List<RestaurantDto> favoriteRestaurants;
    private List<Address> addresses;

    Restaurant restaurant;
    private String restaurantName;
    private String description;
    private String cuisineType;
    private String address;
    private String openingHours;
    private List<String> images;
    private boolean openNow;
    private List<Food> foods;
    private List<ContactInformation> contactInformation;
    private String mobile;
    private String website;
    private String twitter;
    private String instagram;

}
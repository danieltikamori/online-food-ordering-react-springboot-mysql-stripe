package me.amlu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.amlu.dto.FoodDto;
import me.amlu.dto.RestaurantDto;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AnonymizedData {
    // Define the properties and methods of the class

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
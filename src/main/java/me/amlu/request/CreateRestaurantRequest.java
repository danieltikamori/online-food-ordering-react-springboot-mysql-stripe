package me.amlu.request;

import lombok.Data;
import me.amlu.model.ContactInformation;

import java.util.List;

@Data
public class CreateRestaurantRequest {

    private Long id;
    private String restaurantName;
    private String description;
    private String cuisineType;
    private String address;
    private ContactInformation contactInformation;
    private String openingHours;
    private String closingHours;
    private List<String> images;

}

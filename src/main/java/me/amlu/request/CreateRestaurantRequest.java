package me.amlu.request;

import lombok.Data;
import me.amlu.dto.ImageUrlDto;
import me.amlu.model.Address;
import me.amlu.model.ContactInformation;

import java.util.List;

@Data
public class CreateRestaurantRequest {

    private Long id;
    private String restaurantName;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<ImageUrlDto> images;

}

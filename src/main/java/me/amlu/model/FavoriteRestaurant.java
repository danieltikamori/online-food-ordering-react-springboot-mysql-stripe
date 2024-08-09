package me.amlu.model;

import jakarta.persistence.*;
import lombok.Data;
import me.amlu.dto.ImageUrlDto;
import me.amlu.dto.RestaurantDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class FavoriteRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId; // Assuming a foreign key for User

    private Long restaurantId; // Assuming you have an ID in Restaurant
    
    
    private String title;
    private String description;
    private LocalDateTime registrationDate;
    private LocalDateTime updateDate;

    @ElementCollection
    @CollectionTable(name = "restaurant_images", joinColumns = @JoinColumn(name = "restaurant_id"))
    private List<ImageUrlDto> images;

    public FavoriteRestaurant() {
        this.registrationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();

        this.images = new ArrayList<>();
    }

    public void addImage(ImageUrlDto image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
    }

    public void removeImage(ImageUrlDto image) {
        if (images != null) {
            images.remove(image);
        }
    }

    // Add getter and setter methods if needed

}
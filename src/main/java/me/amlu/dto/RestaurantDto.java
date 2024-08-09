package me.amlu.dto;

import jakarta.persistence.Column;
import lombok.Data;
import me.amlu.model.FavoriteRestaurant;

import java.util.List;

@Data
public class RestaurantDto extends FavoriteRestaurant {

    private String title;

    @Column(length = 1000)
    private List<ImageUrlDto> images;

    private String description;
    private Long id;

}

package me.amlu.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantDto {

    private String title;

    @Column(length = 1000)
    private List<ImageUrlDto> images;

    private String description;
    private Long id;

}
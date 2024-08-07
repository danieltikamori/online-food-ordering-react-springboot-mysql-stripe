// ImageDto.java
package me.amlu.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ImageUrlDto {

    private String imageUrl;
    // Other relevant image properties (e.g., caption)

}

// ImageDto.java
package me.amlu.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class ImageUrlDto {

    private String imageUrl;
    // Other relevant image properties (e.g., caption)

    public ImageUrlDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

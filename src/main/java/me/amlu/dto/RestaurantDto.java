package me.amlu.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Embeddable
public class RestaurantDto {

    private String title;

    @Column(length = 1000)
    private List<String> images;

    private String description;
    private Long id;



    /**
     * This method is used to compare the equality of two objects.
     * It is overridden from the Object class to provide a custom implementation
     * for the RestaurantDto class.
     *
     * @param o the object to be compared with the current object
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // First, we check if the object being compared is the same as the current object
        // If they are the same, we can immediately return true
        if (this == o) return true;

        // Next, we check if the object being compared is null or if it is not an instance of RestaurantDto
        // If either of these conditions is true, we can immediately return false
        if (o == null || getClass() != o.getClass()) return false;

        // Now, we can safely cast the object being compared to a RestaurantDto
        RestaurantDto that = (RestaurantDto) o;

        // Finally, we compare the id fields of the two objects
        // If they are equal, we return true; otherwise, we return false
        return Objects.equals(id, that.id);
    }

    /**
     * This method is used to generate a hash code for the object.
     * It is overridden from the Object class to provide a custom implementation
     * for the RestaurantDto class.
     *
     * @return a hash code for the object
     */
    @Override
    public int hashCode() {
        // We use the Objects.hash method to generate a hash code based on the id field
        // This is because the id field is used in the equals method to determine equality
        // By using the same field to generate the hash code, we ensure that equal objects have equal hash codes
        return Objects.hash(id);
    }

}

package me.amlu.repository;

import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.restaurantName) LIKE LOWER(CONCAT('%', :searchQuery, '%'))" +
            " OR LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    List<Restaurant> findBySearchQuery(String searchQuery);

    Restaurant findByOwnerId(Long userId);


}

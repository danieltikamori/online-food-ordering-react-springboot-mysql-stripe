package me.amlu.repository;

import me.amlu.model.IngredientsItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientsItemsRepository extends JpaRepository<IngredientsItems, Long> {

    List<IngredientsItems> findByRestaurantId(Long restaurantId);
}

package me.amlu.service;

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.model.Restaurant;
import me.amlu.repository.IngredientCategoryRepository;
import me.amlu.repository.IngredientsItemsRepository;
import me.amlu.service.Exceptions.IngredientCategoryNotFoundException;
import me.amlu.service.Exceptions.IngredientsItemsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImp implements IngredientsService {

    private final IngredientsItemsRepository ingredientsItemsRepository;

    private final IngredientCategoryRepository ingredientCategoryRepository;

    private final RestaurantService restaurantService;

    public IngredientServiceImp(IngredientsItemsRepository ingredientsItemsRepository, IngredientCategoryRepository ingredientCategoryRepository, RestaurantService restaurantService) {
        this.ingredientsItemsRepository = ingredientsItemsRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.restaurantService = restaurantService;
    }

    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setCategoryName(name);
        ingredientCategory.setRestaurant(restaurant);

        return ingredientCategoryRepository.save(ingredientCategory);

    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {

        Optional<IngredientCategory> optionalIngredientCategory = ingredientCategoryRepository.findById(id);
        if (optionalIngredientCategory.isPresent()) {
            return optionalIngredientCategory.get();
        } else {
            throw new IngredientCategoryNotFoundException("Ingredient category not found.");
        }

    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception {

        restaurantService.findRestaurantById(restaurantId);

        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItems createIngredientsItems(Long restaurantId, String ingredientName, Long ingredientCategoryId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory ingredientCategory = findIngredientCategoryById(ingredientCategoryId);

        IngredientsItems ingredientsItems = new IngredientsItems();
        ingredientsItems.setIngredientName(ingredientName);
        ingredientsItems.setIngredientCategory(ingredientCategory);
        ingredientsItems.setRestaurant(restaurant);
        ingredientsItems.setInStock(true);

        IngredientsItems ingredient = ingredientsItemsRepository.save(ingredientsItems);
        ingredientCategory.getIngredients().add(ingredient);
        return ingredient;
    }

    @Override
    public List<IngredientsItems> findRestaurantsIngredients(Long restaurantId) throws Exception {

        return ingredientsItemsRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItems updateStock(Long id) throws Exception {

        Optional<IngredientsItems> optionalIngredientsItems = ingredientsItemsRepository.findById(id);
        if (optionalIngredientsItems.isEmpty()) {
            throw new IngredientsItemsNotFoundException("Ingredient not found.");
        }
        IngredientsItems ingredientsItems = optionalIngredientsItems.get();
        ingredientsItems.setInStock(!ingredientsItems.isInStock());
        return ingredientsItemsRepository.save(ingredientsItems);
    }

    @Override
    public IngredientCategory updateIngredientCategory(Long id, String name) throws Exception {
        return null;
    }

    @Override
    public void deleteIngredientCategory(Long id) throws Exception {

        IngredientCategory ingredientCategory = findIngredientCategoryById(id);
        ingredientCategoryRepository.delete(ingredientCategory);
    }

    @Override
    public void deleteIngredientsItems(Long id) throws Exception {

        IngredientsItems ingredientsItems = ingredientsItemsRepository.findById(id).orElseThrow();
        ingredientsItemsRepository.delete(ingredientsItems);

    }
}

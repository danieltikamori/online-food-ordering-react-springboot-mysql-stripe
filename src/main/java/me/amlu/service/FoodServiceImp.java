package me.amlu.service;

import me.amlu.model.Category;
import me.amlu.model.Food;
import me.amlu.model.Restaurant;
import me.amlu.repository.FoodRepository;
import me.amlu.request.CreateFoodRequest;
import me.amlu.service.Exceptions.DuplicateCategoryException;
import me.amlu.service.Exceptions.FoodNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService {

    private final EntityUniquenessService uniquenessService;

    private final FoodRepository foodRepository;


    public FoodServiceImp(EntityUniquenessService uniquenessService, FoodRepository foodRepository) {
        this.uniquenessService = uniquenessService;
        this.foodRepository = foodRepository;
    }

    @Override
    public Food createFood(CreateFoodRequest createFoodRequest, Category category, Restaurant restaurant) throws Exception {

        if (uniquenessService.isEntityUnique(new Food(), "name", "restaurant", "foodCategory")) {
            throw new DuplicateCategoryException("Food item with name '" + createFoodRequest.getName() +
                    "' already exists in this category and restaurant.");
        }

        Food food = new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);

        food.setName(createFoodRequest.getName());
        food.setDescription(createFoodRequest.getDescription());
        food.setPrice(createFoodRequest.getPrice());
        food.setImages(createFoodRequest.getImages());
        food.setIngredients(createFoodRequest.getIngredients());

        food.setSeasonal(createFoodRequest.isSeasonal());
        food.setVegetarian(createFoodRequest.isVegetarian());

        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {

        Food food = findFoodById(foodId);
        food.setRestaurant(null);

//        foodRepository.delete(food);
        foodRepository.save(food);

    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegetarian,
                                         boolean isNonVegetarian,
                                         boolean isSeasonal,
                                         String foodCategory) {

        List<Food> foodList = foodRepository.findByRestaurantId(restaurantId);

        if(isVegetarian) {
            foodList = filterByVegetarian(foodList, isVegetarian);
        }
        if(isNonVegetarian) {
            foodList = filterByNonVegetarian(foodList, isNonVegetarian);
        }
        if(isSeasonal) {
            foodList = filterBySeasonal(foodList, isSeasonal);
        }
        if(foodCategory != null && !foodCategory.isEmpty()) {
            foodList = filterByCategory(foodList, foodCategory);
        }

//        Another approach instead of the if statements above that required creating new methods:
//            foodList = foodList.stream().filter(food -> food.isVegetarian()).toList();
//        }
//
//        if(isNonVegetarian) {
//            foodList = foodList.stream().filter(food -> !food.isVegetarian()).toList();
//        }
//
//        if(isSeasonal) {
//            foodList = foodList.stream().filter(food -> food.isSeasonal()).toList();
//        }
//
//        if(foodCategory != null) {
//            foodList = foodList.stream().filter(food -> food.getFoodCategory().getCategoryName().equals(foodCategory)).toList();
//        }

        return foodList;
    }

    private List<Food> filterByCategory(List<Food> foodList, String foodCategory) {
        return foodList.stream().filter(food -> {
            if(food.getFoodCategory() != null) {
                return food.getFoodCategory().getCategoryName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foodList, boolean isSeasonal) {
        return foodList.stream().filter(food -> food.isSeasonal()==isSeasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonVegetarian(List<Food> foodList, boolean isNonVegetarian) {
        return foodList.stream().filter(food -> !food.isVegetarian()).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foodList, boolean isVegetarian) {
        return foodList.stream().filter(food -> food.isVegetarian()==isVegetarian).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if(optionalFood.isEmpty()) {
            throw new FoodNotFoundException("Food not exist.");
        }
        return optionalFood.get();
    }

//    @Override
//    public List<Food> findFoodByCategory(Category category) throws Exception {
//
//        try {
//            return foodRepository.findByFoodCategory(category);
//        } catch (Exception e) {
//            throw new CategoryNotFoundException("Category not found.");
//        }
//
//    }
//
//    @Override
//    public List<Food> getAllFoods() {
//        return foodRepository.findAll();
//    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {

        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());

        return foodRepository.save(food);
    }
}

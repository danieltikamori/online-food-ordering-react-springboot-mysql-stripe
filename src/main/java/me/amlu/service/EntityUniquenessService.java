/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import me.amlu.model.Category;
import me.amlu.model.Food;
import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.repository.CategoryRepository;
import me.amlu.repository.FoodRepository;
import me.amlu.repository.IngredientCategoryRepository;
import me.amlu.repository.IngredientsItemsRepository;
import me.amlu.service.exceptions.DuplicateCategoryException;
import me.amlu.service.exceptions.DuplicateFoodException;
import me.amlu.service.exceptions.DuplicateItemException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author Daniel Tikamori
 * <p>
 * This service is used to check if an entity already exists in the database.
 * Avoids duplicate entities in the database. Avoid using @Autowired, this is just an example.
 * @Service public class IngredientServiceImp {
 * <p>
 * private final EntityUniquenessService uniquenessService;
 * // ... other dependencies ...
 * @Autowired public IngredientServiceImp(EntityUniquenessService uniquenessService, / ... other dependencies .../) {
 * this.uniquenessService = uniquenessService;
 * // ... initialize other dependencies ...
 * }
 * <p>
 * // ...
 * <p>
 * public IngredientCategory createIngredientCategory(@NonNull String name, @NonNull Long restaurantId) {
 * IngredientCategory category = new IngredientCategory();
 * category.setCategoryName(name);
 * category.setRestaurant(/ ... /);
 * <p>
 * if (uniquenessService.isEntityUnique(category, "categoryName", "restaurantId")) {
 * throw new DuplicateCategoryException("Category already exists");
 * }
 * <p>
 * return ingredientCategoryRepository.save(category);
 * }
 * }
 */


@Service
public class EntityUniquenessService {

    private final ApplicationContext applicationContext;

    private final IngredientsItemsRepository ingredientsItemsRepository;

    private final IngredientCategoryRepository ingredientCategoryRepository;

    private final CategoryRepository categoryRepository;

    private final FoodRepository foodRepository;


    //    @Autowired
    public EntityUniquenessService(ApplicationContext applicationContext, IngredientsItemsRepository ingredientsItemsRepository, IngredientCategoryRepository ingredientCategoryRepository, CategoryRepository categoryRepository, FoodRepository foodRepository) {
        this.applicationContext = applicationContext;
        this.ingredientsItemsRepository = ingredientsItemsRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.foodRepository = foodRepository;
    }

    public boolean isEntityUnique(Object entity, String... fieldsToCheck) {
        Object repository = getRepositoryForEntity(entity.getClass());
        return checkIfEntityExists(repository, entity, fieldsToCheck);
    }

    private Object getRepositoryForEntity(Class<?> entityClass) {
        String repositoryName = entityClass.getSimpleName() + "Repository";
        // Convert the first letter to lowercase
        repositoryName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
        return applicationContext.getBean(repositoryName);
    }

    private boolean checkIfEntityExists(Object repository, Object entity, String... fieldsToCheck) {
        try {
            for (String fieldName : fieldsToCheck) {
                Field field = entity.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object fieldValue = field.get(entity);

                // Construct the method name dynamically (e.g., "findByCategoryNameAndRestaurantId")
                String methodName = "findBy" + String.join("And", Arrays.stream(fieldsToCheck).map(this::capitalizeFirstLetter).toArray(String[]::new));

                // Get the method with the correct parameter types
                Class<?>[] parameterTypes = Arrays.stream(fieldsToCheck)
                        .map(f -> {
                            try {
                                return entity.getClass().getDeclaredField(f).getType();
                            } catch (NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toArray(Class<?>[]::new);

                // Create an array of values for each field
                Object[] fieldValues = Arrays.stream(fieldsToCheck)
                        .map(f -> {
                            try {
                                Field fieldToGet = entity.getClass().getDeclaredField(f);
                                fieldToGet.setAccessible(true);
                                return fieldToGet.get(entity);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toArray(Object[]::new);

                Object existingEntity = repository.getClass().getMethod(methodName, parameterTypes)
                        .invoke(repository, fieldValues); // Pass an array of field values

                if (existingEntity != null) {
                    return true; // Duplicate found
                }
            }
        } catch (Exception e) {
            // Handle exceptions appropriately
            throw new RuntimeException("Error checking for entity uniqueness", e);
        }
        return false; // No duplicates found
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void checkUniqueCategory(Category category) throws DuplicateCategoryException {
        if (categoryRepository.existsByCategoryNameAndRestaurantId(
                category.getCategoryName(),
                category.getRestaurant().getRestaurant_id() // Use restaurant ID
        )) {
            throw new DuplicateCategoryException("Category name '" + category.getCategoryName() + "' already exists");
        }
    }

    public void checkUniqueIngredientCategory(IngredientCategory ingredientCategory) throws DuplicateCategoryException {
        if (ingredientCategoryRepository.existsByCategoryNameAndRestaurantId(
                ingredientCategory.getCategoryName(),
                ingredientCategory.getRestaurant().getRestaurant_id() // Use restaurant ID
        )) {
            throw new DuplicateCategoryException("Ingredient category name '" + ingredientCategory.getCategoryName() + "' already exists");
        }

    }

    public void checkUniqueIngredientItem(IngredientsItems ingredientsItems) throws DuplicateItemException {
        if (ingredientsItemsRepository.existsByIngredientNameAndIngredientCategoryAndRestaurant(
                ingredientsItems.getIngredientName(),
                ingredientsItems.getIngredientCategory(),
                ingredientsItems.getRestaurant()
        )) {
            throw new DuplicateItemException("Ingredient item with name '" +
                    ingredientsItems.getIngredientName() + "' already exists in this category.");
        }
    }

    public void checkUniqueFood(Food food) throws DuplicateFoodException {
        if (foodRepository.existsByNameAndFoodCategoryAndRestaurant(
                food.getName(),
                food.getFoodCategory(),
                food.getRestaurant()
        )) {
            throw new DuplicateFoodException("Food item with name '" + food.getName() + "' already exists in this category.");
        }

    }
}
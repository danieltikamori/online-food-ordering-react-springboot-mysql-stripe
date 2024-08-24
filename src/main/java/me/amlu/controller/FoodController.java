package me.amlu.controller;

import me.amlu.model.Food;
import me.amlu.model.User;
import me.amlu.service.FoodService;
import me.amlu.service.RestaurantService;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food")
public class FoodController {

    private final FoodService foodService;

    private final UserService userService;

    private final RestaurantService restaurantService;


    public FoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                           @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);

        List<Food> searchedFoods = foodService.searchFood(name);

        return new ResponseEntity<>(searchedFoods, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFood(@RequestParam boolean vegetarian,
                                                 @RequestParam boolean nonVegetarian,
                                                 @RequestParam boolean seasonal,
                                                 @RequestParam(required = false) String food_category,
                                                 @PathVariable Long restaurantId,
                                                 @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);

        List<Food> foods = foodService.getRestaurantsFood(restaurantId, vegetarian, nonVegetarian, seasonal, food_category);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

}

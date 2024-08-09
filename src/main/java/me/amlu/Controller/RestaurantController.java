package me.amlu.Controller;

import me.amlu.dto.RestaurantDto;
import me.amlu.model.FavoriteRestaurant;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.service.RestaurantNotFoundException;
import me.amlu.service.RestaurantService;
import me.amlu.service.UserNotFoundException;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final UserService userService;

    public RestaurantController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(
            @RequestHeader("Authorization") String token,
            @RequestParam String keyword)
            throws RestaurantNotFoundException, UserNotFoundException {
        User user = userService.findUserByJwtToken(token);

        List<Restaurant> restaurants = restaurantService.searchRestaurant(keyword);
        if (restaurants == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Restaurant>> getAllRestaurants(
            @RequestHeader("Authorization") String token)
            throws RestaurantNotFoundException, UserNotFoundException {
        User user = userService.findUserByJwtToken(token);

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        if (restaurants == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id)
            throws Exception {
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PutMapping("/{id}/add-favorites")
    public ResponseEntity<RestaurantDto> addRestaurantToFavorites(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id)
            throws Exception {
        User user = userService.findUserByJwtToken(token);

        RestaurantDto restaurant = restaurantService.addToFavorites(id, user);
        if (restaurant == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}

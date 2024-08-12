package me.amlu.service;

import me.amlu.dto.RestaurantDto;
import me.amlu.model.Address;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.AddressRepository;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import me.amlu.request.CreateRestaurantRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public RestaurantServiceImp(RestaurantRepository restaurantRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest restaurantRequest, User user) {

        try {
            Address address = addressRepository.save(restaurantRequest.getAddress());
            Restaurant restaurant = new Restaurant();
            restaurant.setAddress(address);
            restaurant.setOwner(user);
            restaurant.setRestaurantName(restaurantRequest.getRestaurantName());
            restaurant.setCuisineType(restaurantRequest.getCuisineType());
            restaurant.setDescription(restaurantRequest.getDescription());
            restaurant.setContactInformation(restaurantRequest.getContactInformation());
            restaurant.setOpeningHours(restaurantRequest.getOpeningHours());
            restaurant.setImages(restaurantRequest.getImages());
            restaurant.setRegistrationDate(LocalDateTime.now());
            restaurant.setUpdateDate(LocalDateTime.now());

            return restaurantRepository.save(restaurant);
        } catch (ConstraintViolationException e) {

            throw new RuntimeException("Constraint violation occurred: " + e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {

            throw new RuntimeException("Data integrity violation occurred: " + e.getMessage(), e);
        }
    }

    // RestaurantServiceImp.java
    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant = optionalRestaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));

        // Using reflection to update the fields
        for (Field field : updatedRestaurant.getClass().getDeclaredFields()) {
            if (!field.getName().equals("id")) { // Ignore ID
                try {
                    field.setAccessible(true);
                    Object newValue = field.get(updatedRestaurant);
                    if (newValue != null) {
                        field.set(restaurant, newValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId);
                }
            }
        }
//        Restaurant restaurant = findRestaurantById(restaurantId);
//        if(restaurant.getCuisineType() != null) {
//            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
//        }
//        if(restaurant.getDescription() != null) {
//            restaurant.setDescription(updatedRestaurant.getDescription());
//        }
//        if(restaurant.getContactInformation() != null) {
//            restaurant.setContactInformation(updatedRestaurant.getContactInformation());
//        }
//        if(restaurant.getOpeningHours() != null) {
//            restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());
//        }
//        if(restaurant.getImages() != null) {
//            restaurant.setImages(updatedRestaurant.getImages());
//        }
//        if(restaurant.getRestaurantName() != null) {
//            restaurant.setRestaurantName(updatedRestaurant.getRestaurantName());
//        }

        return restaurantRepository.save(restaurant);
    }


    @Override
    public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurant.setOpenNow(!restaurant.isOpenNow());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant = optionalRestaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));

//        Restaurant restaurant = findRestaurantById(restaurantId);
//
//        if (restaurant == null) {
//            throw new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId);
//        }

        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant findRestaurantById(Long restaurantId) throws RestaurantNotFoundException {

            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

            if (optionalRestaurant.isPresent()) {
                return optionalRestaurant.get();
            } else {
                throw new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId);
            }
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Optional<Restaurant> getRestaurantsByUserId(Long userId) {
        return restaurantRepository.findByOwnerId(userId);
    }


    @Override
    public List<Restaurant> getRestaurantsByCategory(String category) throws Exception {
        return List.of();
    }

    @Override
    public List<Restaurant> getRestaurantsByCuisineType(String cuisineType) throws Exception {
        return List.of();
    }

    @Override
    public List<Restaurant> getRestaurantsByAddress(String address) throws Exception {
        return List.of();
    }

    @Override
    public List<Restaurant> getRestaurantsByOpeningHours(String openingHours) throws Exception {
        return List.of();
    }

    @Override
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setTitle(restaurant.getRestaurantName());
        restaurantDto.setImages(restaurant.getImages());
        restaurantDto.setId(restaurantId);

        if(user.getFavoriteRestaurants().contains(restaurantDto)) {
            user.getFavoriteRestaurants().remove(restaurantDto);
        }
        else user.getFavoriteRestaurants().add(restaurantDto);

//        ## Another approach instead of the if statement above that required
//        to override the equals and hashCode methods:

//        boolean isFavorited = false;
//        List<RestaurantDto> favoriteRestaurants = user.getFavoriteRestaurants();
//        for (RestaurantDto favoriteRestaurant : favoriteRestaurants) {
//            if (favoriteRestaurant.getId().equals(restaurantId)) {
//                isFavorited = true;
//                break;
//            }
//        }
//        if (isFavorited) {
//            favoriteRestaurants.removeIf(favoriteRestaurant -> favoriteRestaurant.getId().equals(restaurantId));
//        } else {
//            favoriteRestaurants.add(restaurantDto);
//        }

        userRepository.save(user);
        return restaurantDto;
    }
}

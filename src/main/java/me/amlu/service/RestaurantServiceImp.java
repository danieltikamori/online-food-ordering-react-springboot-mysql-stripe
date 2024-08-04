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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            Address address = new Address();
            address.setAddress(restaurantRequest.getAddress()); // Assuming there's a setter method for the address
            address = addressRepository.save(address);
            Restaurant restaurant = new Restaurant();
            restaurant.setAddress(address);
            restaurant.setOwner(user);
            restaurant.setRestaurantName(restaurantRequest.getRestaurantName());
            restaurant.setCuisineType(restaurantRequest.getCuisineType());
            restaurant.setDescription(restaurantRequest.getDescription());
            restaurant.setContactInformation(restaurantRequest.getContactInformation());
            restaurant.setOpeningHours(restaurantRequest.getOpeningHours());
            restaurant.setClosingHours(restaurantRequest.getClosingHours());
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

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant;
        if (optionalRestaurant.isPresent()) {
            restaurant = optionalRestaurant.get();
            // Rest of the code
        } else {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId);
        }

        if (restaurant.getCuisineType() != null) {
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
        }
        if (restaurant.getRestaurantName() != null) {
            restaurant.setRestaurantName(updatedRestaurant.getRestaurantName());
        }
        if (restaurant.getDescription() != null) {
            restaurant.setDescription(updatedRestaurant.getDescription());
        }
        if (restaurant.getContactInformation() != null) {
            restaurant.setContactInformation(updatedRestaurant.getContactInformation());
        }
        if (restaurant.getOpeningHours() != null) {
            restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());
        }
        if (restaurant.getClosingHours() != null) {
            restaurant.setClosingHours(updatedRestaurant.getClosingHours());
        }
        if (restaurant.getImages() != null) {
            restaurant.setImages(updatedRestaurant.getImages());
        }
        if (restaurant.getUpdateDate() != null) {
            restaurant.setUpdateDate(LocalDateTime.now());
        }
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

        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return List.of();
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
    public Restaurant getRestaurantById(Long id) throws Exception {
        return null;
    }

    @Override
    public List<Restaurant> getRestaurantsByUserId(Long userId) throws Exception {

        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if (restaurant == null) {
            throw new RestaurantNotFoundException("Restaurant not found with owner ID: " + userId);
        }
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        return restaurants;
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
    public List<Restaurant> getRestaurantsByClosingHours(String closingHours) throws Exception {
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

        if(user.getFavorites().contains(restaurantDto)) {
            user.getFavorites().remove(restaurantDto);
        }
        else user.getFavorites().add(restaurantDto);

        userRepository.save(user);
        return restaurantDto;
    }
}

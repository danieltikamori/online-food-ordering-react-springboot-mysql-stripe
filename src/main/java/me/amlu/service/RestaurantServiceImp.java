/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import me.amlu.dto.RestaurantDto;
import me.amlu.model.Address;
import me.amlu.model.Restaurant;
import me.amlu.model.User;
import me.amlu.repository.AddressRepository;
import me.amlu.repository.RestaurantRepository;
import me.amlu.repository.UserRepository;
import me.amlu.request.CreateRestaurantRequest;
import me.amlu.service.exceptions.RestaurantNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Slf4j
@Service
public class RestaurantServiceImp implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public static final Logger logger = LogManager.getLogger(RestaurantServiceImp.class);


    public RestaurantServiceImp(RestaurantRepository restaurantRepository, AddressRepository addressRepository, UserRepository userRepository, NotificationService notificationService) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public Restaurant createRestaurant(@NonNull CreateRestaurantRequest restaurantRequest, User user) {

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
            restaurant.setImagesURL((CopyOnWriteArrayList<String>) restaurantRequest.getImages());
            restaurant.setCreatedAt(Instant.now());
            restaurant.setUpdatedAt(Instant.now());
            restaurant.setCreatedBy(user);
            restaurant.setUpdatedBy(user);


            return restaurantRepository.save(restaurant);
        } catch (ConstraintViolationException e) {
            logger.error("Constraint violation occurred: {}", e.getMessage());
            throw new RuntimeException("Constraint violation occurred: " + e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation occurred: {}", e.getMessage());
            notificationService.sendNotification("Data integrity violation occurred", e.getMessage());
            throw new RuntimeException("Data integrity violation occurred: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Restaurant updateRestaurant(Long restaurantId, @NonNull CreateRestaurantRequest updatedRestaurant) throws Exception {
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

        restaurant.setUpdatedAt(Instant.now());
        restaurant.setUpdatedBy(getAuthenticatedUser());

        return restaurantRepository.save(restaurant);
    }


    @Override
    @Transactional
    public Restaurant updateRestaurantStatus(Long restaurantId) throws Exception {

        try {
            Restaurant restaurant = findRestaurantById(restaurantId);
            restaurant.setOpenNow(!restaurant.isOpenNow());
            restaurant.setUpdatedAt(Instant.now());
            restaurant.setUpdatedBy(getAuthenticatedUser());

            return restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId);
        }
    }

    @Override
    @Transactional
    public void deleteRestaurant(Long restaurantId) throws Exception {

        try {
            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
            Restaurant restaurant = optionalRestaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));

            restaurant.setDeletedAt(Instant.now());
            restaurant.setDeletedBy(getAuthenticatedUser());

            restaurantRepository.delete(restaurant);
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation occurred when deleting restaurant: {}", e.getMessage());
            notificationService.sendNotification("Data integrity violation occurred when deleting restaurant.", e.getMessage());
            throw new RuntimeException("Data integrity violation occurred: " + e.getMessage(), e);
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    @Async
    @Transactional
    public void permanentlyDeleteRestaurant(Long restaurantId) throws Exception {

        try {
            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
            Restaurant restaurant = optionalRestaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));

            restaurantRepository.deleteById(restaurantId);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation occurred: {}", e.getMessage());
            logger.error("Data integrity violation occurred when permanently deleting restaurant: {}", e.getMessage());
            notificationService.sendNotification("Data integrity violation occurred", e.getMessage());
            throw new RuntimeException("Data integrity violation occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant findRestaurantById(Long restaurantId) throws RestaurantNotFoundException {

        try {
            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
            return optionalRestaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));
        } catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId);
        }
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) throws RestaurantNotFoundException {

        try{
        return restaurantRepository.findBySearchQueryIgnoreCase(keyword)
                .stream()
                .filter(restaurant -> restaurant.getDeletedAt() == null) // Filter out deleted restaurants
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + keyword);
        }
    }

    @Override
    public Optional<Restaurant> getRestaurantsByUserId(Long userId) throws RestaurantNotFoundException {
        try {
            return restaurantRepository.findByOwnerId(userId);
        }
        catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + userId);}
    }


    @Override
    public Optional<Object> getRestaurantsByCategory(String category) throws Exception {

        try{
            return restaurantRepository.findByCategory(category);
        } catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + category);
        }

    }

    @Override
    public List<Restaurant> getRestaurantsByCuisineType(String cuisineType) throws Exception {
        try {
            return restaurantRepository.findByCuisineType(cuisineType);
        } catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + cuisineType);
        }
    }

    @Override
    public List<Restaurant> getRestaurantsByCity(String city) throws Exception {
        try {
            return restaurantRepository.findByAddress_City(city);
        } catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + city);
        }
    }

    @Override
    public List<Restaurant> getRestaurantsByOpeningHours(String openingHours) throws Exception {

        // TODO: Implement search by opening hours
        try {
            return restaurantRepository.findByOpeningHours(openingHours);
        } catch (Exception e) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + openingHours);
        }
    }

    @Override
    @Transactional
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setRestaurantName(restaurant.getRestaurantName());
        restaurantDto.setImagesURL(restaurant.getImagesURL());
        restaurantDto.setRestaurant_id(restaurantId);

        if (user.getFavoriteRestaurants().contains(restaurantDto)) {
            user.getFavoriteRestaurants().remove(restaurantDto);
        } else {
            user.getFavoriteRestaurants().add(restaurantDto);
        }

//        ## Another approach instead of the if statement above that required
//        to override the equals and hashCode methods:

//        boolean isFavorited = false;
//        List<RestaurantDto> favoriteRestaurants = user.getFavoriteRestaurants();
//        for (RestaurantDto favoriteRestaurant : favoriteRestaurants) {
//            if (favoriteRestaurant.getCategory_id().equals(restaurantId)) {
//                isFavorited = true;
//                break;
//            }
//        }
//        if (isFavorited) {
//            favoriteRestaurants.removeIf(favoriteRestaurant -> favoriteRestaurant.getCategory_id().equals(restaurantId));
//        } else {
//            favoriteRestaurants.add(restaurantDto);
//        }

        userRepository.save(user);
        return restaurantDto;
    }
}

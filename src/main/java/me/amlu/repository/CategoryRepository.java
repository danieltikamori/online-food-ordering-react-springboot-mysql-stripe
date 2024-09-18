/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.repository;

import me.amlu.model.Category;
import me.amlu.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends BaseRepository<Category, Long> {

    Category findByCategoryName(String categoryName);

    List<Category> findCategoryByRestaurantId(Long restaurantId);

    Category findCategoryById(Long categoryId);

    List<Category> findByRestaurantId(Long id);

    Category findByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) = LOWER(:categoryName) AND c.deletedAt IS NULL")
    Category findSimilarCategoryIgnoreCase(@Param("categoryName") String categoryName);

    boolean existsByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    boolean existsByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);

}
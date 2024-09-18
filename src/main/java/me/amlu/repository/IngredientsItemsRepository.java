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

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.model.Restaurant;

import java.util.Optional;
import java.util.Set;

public interface IngredientsItemsRepository extends BaseRepository<IngredientsItems, Long> {

    Set<IngredientsItems> findByRestaurantId(Long restaurantId);

    // Specific method for uniqueness check
    Optional<IngredientsItems> findByIngredientNameAndIngredientCategoryAndRestaurant(String ingredientName,
                                                                                                        IngredientCategory ingredientCategory, Restaurant restaurant);

    boolean existsByIngredientNameAndIngredientCategoryAndRestaurant(String ingredientName,
                                                                                       IngredientCategory ingredientCategory,
                                                                                       Restaurant restaurant);

    boolean existsByIngredientNameAndIngredientCategoryIdAndRestaurantId(String ingredientName, Long ingredientCategoryId, Long restaurantId);
}

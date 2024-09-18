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
import me.amlu.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface IngredientCategoryRepository extends BaseRepository<IngredientCategory, Long> {

    List<IngredientCategory> findByRestaurantId(Long restaurantId);
    
    Optional<IngredientCategory> findByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);

    IngredientCategory findByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    boolean existsByCategoryNameAndRestaurant(String categoryName, Restaurant restaurant);

    boolean existsByCategoryNameAndRestaurantId(String categoryName, Long restaurantId);
}

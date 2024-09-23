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

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;

import java.util.List;
import java.util.Set;

public interface IngredientsService {

    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;

    public IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception;

    public IngredientsItems createIngredientsItems(Long restaurantId, String ingredientName, Long ingredientCategoryId) throws Exception;

    public Set<IngredientsItems> findRestaurantsIngredients(Long restaurantId) throws Exception;

    public IngredientsItems updateStock(Long id) throws Exception;

    public IngredientCategory updateIngredientCategory(Long id, String name) throws Exception;

    public void deleteIngredientCategory(Long id) throws Exception;

    public void deleteIngredientsItems(Long id) throws Exception;
}

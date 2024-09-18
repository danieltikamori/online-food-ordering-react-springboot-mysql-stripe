/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.controller;

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.request.IngredientCategoryRequest;
import me.amlu.request.IngredientRequest;
import me.amlu.service.IngredientsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/ingredients")
public class IngredientController {

    IngredientsService ingredientsService;

    public IngredientController(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @Valid @RequestBody IngredientCategoryRequest request
            ) throws Exception {

        IngredientCategory category = ingredientsService.createIngredientCategory(request.getCategoryName(), request.getRestaurantId());

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @PostMapping("/items")
    public ResponseEntity<IngredientsItems> createIngredientsItems(
            @Valid @RequestBody IngredientRequest request
    ) throws Exception {

        IngredientsItems item = ingredientsService.createIngredientsItems(request.getRestaurantId(), request.getItemName(), request.getCategoryId());

        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/{category_id}/stock")
    public ResponseEntity<IngredientsItems> updateStock(
            @PathVariable Long id
    ) throws Exception {
        IngredientsItems item = ingredientsService.updateStock(id);

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/category/{category_id}")
    public ResponseEntity<IngredientCategory> updateIngredientCategory(
            @PathVariable Long id,
            @Valid @RequestBody String name
    ) throws Exception {
        IngredientCategory category = ingredientsService.updateIngredientCategory(id, name);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/category/{category_id}")
    public ResponseEntity<Void> deleteIngredientCategory(
            @PathVariable Long id
    ) throws Exception {
        ingredientsService.deleteIngredientCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<Void> deleteIngredientsItems(
            @PathVariable Long id
    ) throws Exception {
        ingredientsService.deleteIngredientsItems(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/restaurant/{id}")
    public ResponseEntity<Set<IngredientsItems>> getRestaurantsIngredients(
            @PathVariable Long id
    ) throws Exception {
        Set<IngredientsItems> items = Collections.synchronizedSet(ingredientsService.findRestaurantsIngredients(id));

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantCategory(
            @PathVariable Long id
    ) throws Exception {
        List<IngredientCategory> categories = ingredientsService.findIngredientCategoryByRestaurantId(id);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }



}

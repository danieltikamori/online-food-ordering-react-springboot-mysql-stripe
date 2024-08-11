package me.amlu.Controller;

import me.amlu.model.IngredientCategory;
import me.amlu.model.IngredientsItems;
import me.amlu.request.IngredientCategoryRequest;
import me.amlu.request.IngredientRequest;
import me.amlu.service.IngredientsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/ingredients")
public class IngredientController {

    IngredientsService ingredientsService;

    public IngredientController(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @RequestBody IngredientCategoryRequest request
            ) throws Exception {

        IngredientCategory category = ingredientsService.createIngredientCategory(request.getCategoryName(), request.getRestaurantId());

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @PostMapping()
    public ResponseEntity<IngredientsItems> createIngredientsItems(
            @RequestBody IngredientRequest request
    ) throws Exception {

        IngredientsItems item = ingredientsService.createIngredientsItems(request.getRestaurantId(), request.getItemName(), request.getCategoryId());

        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<IngredientsItems> updateStock(
            @PathVariable Long id
    ) throws Exception {
        IngredientsItems item = ingredientsService.updateStock(id);

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<IngredientCategory> updateIngredientCategory(
            @PathVariable Long id,
            @RequestBody String name
    ) throws Exception {
        IngredientCategory category = ingredientsService.updateIngredientCategory(id, name);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteIngredientCategory(
            @PathVariable Long id
    ) throws Exception {
        ingredientsService.deleteIngredientCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientsItems(
            @PathVariable Long id
    ) throws Exception {
        ingredientsService.deleteIngredientsItems(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientsItems>> getRestaurantsIngredients(
            @PathVariable Long id
    ) throws Exception {
        List<IngredientsItems> items = ingredientsService.findRestaurantsIngredients(id);

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

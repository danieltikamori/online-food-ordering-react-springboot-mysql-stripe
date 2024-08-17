package me.amlu.Controller;

import me.amlu.model.Category;
import me.amlu.model.User;
import me.amlu.service.CategoryService;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category,
                                                   @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        Category existingCategory = categoryService.findCategoryByName(category.getCategoryName());

        if (existingCategory != null) {
            // If an existing category is found, return a response with the existing category and a message
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(existingCategory);
        } else {
            // If no existing category is found, try to find a similar category (e.g. with a different case)
            Category similarCategory = categoryService.findSimilarCategory(category.getCategoryName());

            if (similarCategory != null) {
                // If a similar category is found, return a response with the similar category and a message
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(similarCategory);
            } else {
                // If no similar category is found, create a new category
                Category createdCategory = categoryService.createCategory(category.getCategoryName(), user.getId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(createdCategory);
            }
        }
    }


    @GetMapping("/category/restaurant")
    public ResponseEntity<List<Category>> getRestaurantCategory(@RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);
        List<Category> categories = categoryService.findCategoryByRestaurantId(user.getId());

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id,
                                               @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

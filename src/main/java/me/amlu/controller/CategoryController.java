package me.amlu.controller;

import me.amlu.model.Category;
import me.amlu.model.User;
import me.amlu.service.CategoryService;
import me.amlu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category,
                                                   @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);

        // If no existing category is found, create a new category
        Category createdCategory = categoryService.createCategory(category.getCategoryName(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCategory);
    }


    @GetMapping("/category/restaurant/{id}")
    public ResponseEntity<List<Category>> getRestaurantCategory(@PathVariable Long id,
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserByJwtToken(token);
        List<Category> categories = categoryService.findCategoryByRestaurantId(id);

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

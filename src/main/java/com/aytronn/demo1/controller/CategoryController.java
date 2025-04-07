package com.aytronn.demo1.controller;

import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dto.CategoryCreateInput;
import com.aytronn.demo1.service.CategoryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  @GetMapping("/{id}")
  public Category getCategoryById(@PathVariable String id) {
    return categoryService.getCategoryById(id);
  }

  @PostMapping
  public Category createCategory(@RequestBody CategoryCreateInput input) {
    return categoryService.createCategory(input);
  }

  @PutMapping("/{id}")
  public Category updateCategory(@PathVariable String id, @RequestBody CategoryCreateInput input) {
    return categoryService.updateCategory(id, input);
  }
}

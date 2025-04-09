package com.aytronn.demo1.service;

import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dto.CategoryCreateInput;
import com.aytronn.demo1.exception.ApiException;
import com.aytronn.demo1.repository.CategoryRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category getCategoryById(String id) {
    return categoryRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));
  }

  public Category createCategory(CategoryCreateInput input) {
    Category newCategory = Category.builder()
        .name(input.name())
        .build();

    return categoryRepository.save(newCategory);
  }

  public Category updateCategory(String id, CategoryCreateInput input) {
    Category category = categoryRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));

    category.setName(input.name());

    return categoryRepository.save(category);
  }
}

package com.aytronn.demo1.service;

import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dto.CategoryCreateInput;
import com.aytronn.demo1.repository.CategoryRepository;
import java.util.List;
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
    return categoryRepository.findById(id).orElseThrow();
  }

  public Category createCategory(CategoryCreateInput input) {
    Category newCategory = Category.builder()
        .name(input.name())
        .build();

    return categoryRepository.save(newCategory);
  }

  public Category updateCategory(String id, CategoryCreateInput input) {
    Category category = categoryRepository.findById(id).orElseThrow();

    category.setName(input.name());

    return categoryRepository.save(category);
  }
}

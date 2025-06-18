package com.aytronn.demo1.service;

import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dao.City;
import com.aytronn.demo1.dto.CategoryCreateInput;
import com.aytronn.demo1.dto.CategoryDto;
import com.aytronn.demo1.dto.UserDto;
import com.aytronn.demo1.exception.ApiException;
import com.aytronn.demo1.repository.CategoryRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final WebClient webClient;

  public CategoryService(CategoryRepository categoryRepository, WebClient webClient) {
    this.categoryRepository = categoryRepository;
    this.webClient = webClient;
  }

  public List<CategoryDto> getAllCategories() {
    List<Category> all = categoryRepository.findAll();

    Map<String, UserDto> userCache = new HashMap<>();

    return all.stream().map(category -> {
      UserDto createdBy = userCache.get(category.getCreatedBy());
      if (createdBy == null && category.getCreatedBy() != null) {
        createdBy = getUserById(UUID.fromString(category.getCreatedBy()));
        userCache.put(category.getCreatedBy(), createdBy);
      }

      UserDto updatedBy = null;
      if (category.getCreatedBy() != null && category.getCreatedBy().equals(category.getUpdatedBy())) {
        updatedBy = createdBy;
      } else if (category.getUpdatedBy() != null) {
        updatedBy = userCache.get(category.getUpdatedBy());
        if (updatedBy == null) {
          updatedBy = getUserById(UUID.fromString(category.getUpdatedBy()));
        }
      }

      return CategoryDto.builder()
          .id(category.getId())
          .name(category.getName())
          .createdBy(createdBy)
          .updatedBy(updatedBy)
          .build();
    }).toList();
  }

  public CategoryDto getCategoryById(String id) {
    Category categoryNotFound = categoryRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));

    UserDto createdBy = getUserById(UUID.fromString(categoryNotFound.getCreatedBy()));

    UserDto updatedBy = null;
    if (categoryNotFound.getCreatedBy().equals(categoryNotFound.getUpdatedBy())) {
      updatedBy = createdBy;
    } else if (categoryNotFound.getUpdatedBy() != null) {
      updatedBy = getUserById(UUID.fromString(categoryNotFound.getUpdatedBy()));
    }

    return CategoryDto.builder()
        .id(categoryNotFound.getId())
        .name(categoryNotFound.getName())
        .createdBy(createdBy)
        .updatedBy(updatedBy)
        .build();
  }

  public Category createCategory(CategoryCreateInput input, String identifier) {

    UserDto userDto = getUserByEmail(identifier);
    Category newCategory = Category.builder()
        .name(input.name())
        .createdBy(userDto.id().toString())
        .build();

    return categoryRepository.save(newCategory);
  }

  public Category updateCategory(String id, CategoryCreateInput input, String identifier) {
    Category category = categoryRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));

    UserDto userDto = getUserByEmail(identifier);

    category.setName(input.name());
    category.setUpdatedBy(userDto.id().toString());

    return categoryRepository.save(category);
  }

  private UserDto getUserById(UUID id) {
    return webClient
        .get()
        .uri("http://localhost:8081/api/v1/users/{id}", id)
        .retrieve()
        .bodyToMono(UserDto.class)
        .doOnError(e -> {
          throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch city data");
        }).block();
  }

  private UserDto getUserByEmail(String email) {
    return webClient
        .get()
        .uri("http://localhost:8081/api/v1/users/{email}/email", email)
        .retrieve()
        .bodyToMono(UserDto.class)
        .doOnError(e -> {
          throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch city data");
        }).block();
  }
}

package com.aytronn.demo1.service;

import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dao.City;
import com.aytronn.demo1.dto.CategoryCreateInput;
import com.aytronn.demo1.dto.UserDto;
import com.aytronn.demo1.exception.ApiException;
import com.aytronn.demo1.repository.CategoryRepository;
import java.util.List;
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

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category getCategoryById(String id) {
    //TODO: GET USER BY ID WITH WEBCLIENT FOR RETURN A NEW DTO WITH COMPLETED USER DATA
    return categoryRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));
  }

  public Category createCategory(CategoryCreateInput input, String identifier) {

    //TODO: GET USER BY IDENTIFIER (IS MAIL OF USER) WITH WEBCLIENT AND SAVE ID OF SAVE INSTEAD OF MAIL
    Category newCategory = Category.builder()
        .name(input.name())
        .createdBy(identifier)
        .build();

    return categoryRepository.save(newCategory);
  }

  public Category updateCategory(String id, CategoryCreateInput input, String identifier) {
    Category category = categoryRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));

    //TODO: GET USER BY IDENTIFIER (IS MAIL OF USER) WITH WEBCLIENT AND SAVE ID OF SAVE INSTEAD OF MAIL
    category.setName(input.name());
    category.setUpdatedBy(identifier);

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

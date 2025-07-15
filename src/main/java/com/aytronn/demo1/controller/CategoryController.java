package com.aytronn.demo1.controller;

import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dto.CategoryCreateInput;
import com.aytronn.demo1.exception.ApiException;
import com.aytronn.demo1.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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


  @Operation(summary = "Get all categories",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Return a list of all categories",
              content = @Content(
                  schema = @Schema(implementation = Category.class)
              )
          ),
          @ApiResponse(responseCode = "400", description = "The data of the request is invalid", content = @Content)
      })
  @GetMapping
  @PreAuthorize("hasAuthority('admin:read')")
  public List<Category> getAllCategories(Authentication authentication) {
    authentication.getPrincipal();
    return categoryService.getAllCategories();
  }

  @Operation(summary = "Get Category by ID",
      parameters = {
          @Parameter(
              name = "id",
              in = ParameterIn.PATH,
              description = "The unique identifier of the category",
              required = true,
              schema = @Schema(
                  type = "string",
                  example = "123e4567-e89b-12d3-a456-426614174000"
              )
          )
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Return the category with the given ID",
              content = @Content(
                  schema = @Schema(implementation = Category.class)
              )
          ),
          @ApiResponse(responseCode = "400", description = "The data of the request is invalid", content = @Content)
      })
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('admin:read')")
  public Category getCategoryById(@PathVariable String id) {
    return categoryService.getCategoryById(id);
  }

  @Operation(summary = "Create a new category",
      parameters = {
          @Parameter(
              name = "identifier",
              in = ParameterIn.QUERY,
              description = "Email of the user creating the category",
              required = false,
              schema = @Schema(
                  type = "string",
                  example = "test@gmail.com"
              )
          )
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "The category to create",
          content = @Content(
              schema = @Schema(implementation = CategoryCreateInput.class)
          )
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "The category has been created successfully",
              content = @Content(
                  schema = @Schema(implementation = Category.class)
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Already exists a category with the same name",
              content = @Content(
                  schema = @Schema(implementation = ApiException.class)
              )
          ),
      }
  )
  @PostMapping
  @PreAuthorize("hasAuthority('admin:create')")
  public Category createCategory(@RequestBody CategoryCreateInput input) {
    return categoryService.createCategory(input);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('admin:update')")
  public Category updateCategory(@PathVariable String id, @RequestBody CategoryCreateInput input) {
    return categoryService.updateCategory(id, input);
  }
}

package com.aytronn.demo1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CategoryDto {
  private String id;
  private String name;
  private UserDto createdBy;
  private UserDto updatedBy;
}

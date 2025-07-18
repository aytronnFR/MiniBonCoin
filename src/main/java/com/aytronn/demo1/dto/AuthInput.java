package com.aytronn.demo1.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthInput(
    @NotBlank String email,
    @NotBlank String password
) {

}

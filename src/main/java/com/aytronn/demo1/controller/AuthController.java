package com.aytronn.demo1.controller;

import com.aytronn.demo1.dto.UserInput;
import com.aytronn.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<Void> createUser(@RequestBody UserInput input) {
    return userService.createUser(input);
  }
}

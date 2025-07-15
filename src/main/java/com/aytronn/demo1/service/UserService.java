package com.aytronn.demo1.service;

import com.aytronn.demo1.config.security.Role;
import com.aytronn.demo1.dao.User;
import com.aytronn.demo1.dto.UserInput;
import com.aytronn.demo1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public ResponseEntity<Void> createUser(UserInput input) {
    var optionalUser = userRepository.findByEmail(input.email());

    if (optionalUser.isPresent()) {
      throw new RuntimeException("User already exists with this email");
    }

    var newUser = User.builder()
        .email(input.email())
        .password(passwordEncoder.encode(input.password()))
        .role(Role.USER)
        .build();

    userRepository.save(newUser);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}

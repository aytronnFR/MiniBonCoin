package com.aytronn.demo1.service;

import com.aytronn.demo1.config.security.JwtService;
import com.aytronn.demo1.config.security.Role;
import com.aytronn.demo1.dao.User;
import com.aytronn.demo1.dto.AuthInput;
import com.aytronn.demo1.dto.TokenOutput;
import com.aytronn.demo1.dto.UserInput;
import com.aytronn.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthentificationService {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  public TokenOutput authenticate(AuthInput input) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(input.email(), input.password())
    );

    User user = userRepository.findByEmail(input.email())
        .orElseThrow(() -> new RuntimeException("User not found"));

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    return new TokenOutput(jwtToken, refreshToken);
  }

  public ResponseEntity<Void> createUser(UserInput input) {
    var optionalUser = userRepository.findByEmail(input.email());

    if (optionalUser.isPresent()) {
      throw new RuntimeException("User already exists with this email");
    }

    var newUser = User.builder()
        .email(input.email())
        .password(passwordEncoder.encode(input.password()))
        .role(Role.USER)
        .enabled(true)
        .build();

    userRepository.save(newUser);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}

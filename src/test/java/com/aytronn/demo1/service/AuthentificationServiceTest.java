package com.aytronn.demo1.service;

import com.aytronn.demo1.config.security.JwtService;
import com.aytronn.demo1.config.security.Role;
import com.aytronn.demo1.dao.User;
import com.aytronn.demo1.dto.AuthInput;
import com.aytronn.demo1.dto.TokenOutput;
import com.aytronn.demo1.dto.UserInput;
import com.aytronn.demo1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthentificationServiceTest {

  @InjectMocks
  private AuthentificationService authentificationService;

  @Mock
  private JwtService jwtService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void authenticate_shouldReturnTokens_whenUserExistsAndPasswordIsCorrect() {
    // Given
    AuthInput input = new AuthInput("test@example.com", "password");
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .email(input.email())
        .password("hashedPassword")
        .role(Role.USER)
        .enabled(true)
        .build();

    when(userRepository.findByEmail(input.email())).thenReturn(Optional.of(user));
    when(jwtService.generateToken(user)).thenReturn("jwt-token");
    when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

    // When
    TokenOutput result = authentificationService.authenticate(input);

    // Then
    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    assertThat(result.accessToken()).isEqualTo("jwt-token");
    assertThat(result.refreshToken()).isEqualTo("refresh-token");
  }

  @Test
  void authenticate_shouldThrowException_whenUserNotFound() {
    AuthInput input = new AuthInput("notfound@example.com", "password");

    when(userRepository.findByEmail(input.email())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authentificationService.authenticate(input))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("User not found");
  }

  @Test
  void createUser_shouldCreateUser_whenUserDoesNotExist() {
    UserInput input = new UserInput("newuser@example.com", "password");

    when(userRepository.findByEmail(input.email())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(input.password())).thenReturn("encodedPassword");

    ResponseEntity<Void> response = authentificationService.createUser(input);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    verify(userRepository).save(argThat(user ->
        user.getEmail().equals(input.email())
            && user.getPassword().equals("encodedPassword")
            && user.getRole() == Role.USER
            && user.isEnabled()
    ));
  }

  @Test
  void createUser_shouldThrowException_whenUserAlreadyExists() {
    UserInput input = new UserInput("existing@example.com", "password");
    when(userRepository.findByEmail(input.email())).thenReturn(Optional.of(mock(User.class)));

    assertThatThrownBy(() -> authentificationService.createUser(input))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("User already exists");
  }
}

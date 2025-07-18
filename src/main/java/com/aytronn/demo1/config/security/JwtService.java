package com.aytronn.demo1.config.security;

import com.aytronn.demo1.dao.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

  @Value("${security.secret-key}")
  private String secretKey;
  @Value("${security.token-expiration}")
  private long jwtExpiration;
  @Value("${security.refresh-token-expiration}")
  private long refreshExpiration;

  public String generateToken(User userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("id", userDetails.getId());
    extraClaims.put("role", userDetails.getRole().name());
    extraClaims.put(
        "permissions", userDetails.getRole().getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList()
    );

    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(Map.of(), userDetails, refreshExpiration);
  }

  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      long expiration
  ) {
    return Jwts
        .builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public boolean validate(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token - {}", ex.getMessage());
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token - {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token - {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty - {}", ex.getMessage());
    }
    return false;
  }

  public String getUsername(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  public Date getExpirationDate(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getExpiration();
  }

  public boolean isTokenExpired(String token) {
    return getExpirationDate(token).before(new Date());
  }
}

package com.aytronn.demo1.repository;

import com.aytronn.demo1.dao.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

  @Query(
      nativeQuery = true,
      value = "SELECT * FROM \"user\" WHERE email = ?1"
  )
  Optional<User> findByEmail(String email);
}

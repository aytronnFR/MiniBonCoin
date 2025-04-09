package com.aytronn.demo1.repository;

import com.aytronn.demo1.dao.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

  Optional<Category> findByName(String name);
}

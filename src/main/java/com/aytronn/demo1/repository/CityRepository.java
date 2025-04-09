package com.aytronn.demo1.repository;

import com.aytronn.demo1.dao.City;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, String> {

  Optional<City> findByName(String name);
}

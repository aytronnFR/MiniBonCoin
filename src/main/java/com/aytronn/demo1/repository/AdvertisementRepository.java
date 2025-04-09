package com.aytronn.demo1.repository;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dao.City;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {

  List<Advertisement> findByCategory(Category category);

  List<Advertisement> findByCity(City city);

  List<Advertisement> findByTitleContaining(String title);

  List<Advertisement> findByCategory_Name(String categoryName);

  List<Advertisement> findByCity_Name(String name);
}

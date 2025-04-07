package com.aytronn.demo1.repository;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dao.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {

  List<Advertisement> findByCategory(Category category);
}

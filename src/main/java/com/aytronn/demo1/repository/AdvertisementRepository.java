package com.aytronn.demo1.repository;

import com.aytronn.demo1.dao.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AdvertisementRepository extends JpaRepository<Advertisement, String>,
    QuerydslPredicateExecutor<Advertisement> {
}

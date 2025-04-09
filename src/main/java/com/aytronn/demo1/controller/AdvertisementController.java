package com.aytronn.demo1.controller;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dao.QAdvertisement;
import com.aytronn.demo1.dto.AdvertisementInput;
import com.aytronn.demo1.dto.AdvertisementOutput;
import com.aytronn.demo1.service.AdvertisementService;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/advertisements")
public class AdvertisementController {


  private final AdvertisementService advertisementService;

  public AdvertisementController(AdvertisementService advertisementService) {
    this.advertisementService = advertisementService;
  }

  @GetMapping
  public Page<Advertisement> getAllAdvertisements(
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String cityName,
      @RequestParam(required = false) String titleContain,
      @RequestParam(required = false) double minPrice,
      @RequestParam(required = false) double maxPrice,
      @RequestParam(required = false) Instant minCreatedAt,
      @RequestParam(required = false) Instant maxCreatedAt,
      Pageable pageable
  ) {
    QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    BooleanExpression predicate = qAdvertisement.isNotNull();

    if (categoryId != null) {
      predicate = predicate.and(qAdvertisement.category.id.eq(categoryId));
    }
    if (cityName != null) {
      predicate = predicate.and(qAdvertisement.city.name.eq(cityName));
    }
    if (titleContain != null) {
      predicate = predicate.and(qAdvertisement.title.containsIgnoreCase(titleContain));
    }
    if (minPrice > 0) {
      predicate = predicate.and(qAdvertisement.price.goe(minPrice));
    }
    if (maxPrice > 0) {
      predicate = predicate.and(qAdvertisement.price.loe(maxPrice));
    }
    if (minCreatedAt != null) {
      predicate = predicate.and(qAdvertisement.createdAt.after(minCreatedAt));
    }
    if (maxCreatedAt != null) {
      predicate = predicate.and(qAdvertisement.createdAt.before(maxCreatedAt));
    }

    return advertisementService.getAllAdvertisement(pageable, predicate);

  }

  @PostMapping
  public Advertisement createAdvertisement(@RequestBody AdvertisementInput input) {
    return advertisementService.createAdvertisement(input);
  }


  @GetMapping("/{id}")
  public AdvertisementOutput getAdvertisementById(@PathVariable String id) {
    return advertisementService.getAdvertisementById(id);
  }

  @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Advertisement addPhotoToAdvertisement(@PathVariable String id, @RequestParam MultipartFile image) {
    return advertisementService.addPhotoToAdvertisement(id, image);
  }

  @PutMapping("/{id}")
  public Advertisement updateAdvertisement(@PathVariable String id, @RequestBody AdvertisementInput input) {
    return advertisementService.updateAdvertisement(id, input);
  }

  @DeleteMapping("/{id}")
  public void deleteAdvertisement(@PathVariable String id) {
    advertisementService.deleteAdvertisement(id);
  }
}

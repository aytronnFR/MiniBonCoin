package com.aytronn.demo1.service;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dao.AdvertisementImage;
import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dao.City;
import com.aytronn.demo1.dto.AdvertisementInput;
import com.aytronn.demo1.dto.AdvertisementOutput;
import com.aytronn.demo1.exception.ApiException;
import com.aytronn.demo1.mapper.AdvertisementMapper;
import com.aytronn.demo1.repository.AdvertisementImageRepository;
import com.aytronn.demo1.repository.AdvertisementRepository;
import com.aytronn.demo1.repository.CategoryRepository;
import com.aytronn.demo1.repository.CityRepository;
import com.querydsl.core.types.Predicate;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdvertisementService {

  private final AdvertisementRepository advertisementRepository;
  private final CategoryRepository categoryRepository;
  private final AdvertisementImageRepository advertisementImageRepository;
  private final CityRepository cityRepository;
  private final AdvertisementMapper advertisementMapper;

  public AdvertisementService(AdvertisementRepository advertisementRepository, CategoryRepository categoryRepository,
      AdvertisementImageRepository advertisementImageRepository, CityRepository cityRepository,
      AdvertisementMapper advertisementMapper) {
    this.advertisementRepository = advertisementRepository;
    this.categoryRepository = categoryRepository;
    this.advertisementImageRepository = advertisementImageRepository;
    this.cityRepository = cityRepository;
    this.advertisementMapper = advertisementMapper;
  }

  public Advertisement createAdvertisement(AdvertisementInput input) {
    Category category = categoryRepository.findById(input.categoryId()).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));

    Optional<City> optionalCity = cityRepository.findByName(input.cityName());

    City city = createOrgetCity(input, optionalCity);

    Advertisement newAdvertisement = Advertisement.builder()
        .title(input.title())
        .description(input.description())
        .category(category)
        .city(city)
        .createdAt(Instant.now())
        .build();

    return advertisementRepository.save(newAdvertisement);
  }

  private City createOrgetCity(AdvertisementInput input, Optional<City> optionalCity) {
    if (optionalCity.isEmpty()) {
      City build = City.builder().name(input.cityName()).build();
      return cityRepository.save(build);
    }
    return optionalCity.get();
  }

  public Page<Advertisement> getAllAdvertisement(Pageable pageable, Predicate predicate) {
    return advertisementRepository.findAll(predicate, pageable);
  }

  public AdvertisementOutput getAdvertisementById(String id) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Advertisement not found"
    ));

    return advertisementMapper.toAdvertisementOutput(advertisement);
  }

  public Advertisement updateAdvertisement(String id, AdvertisementInput input) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Advertisement not found"
    ));

    Category category = categoryRepository.findById(input.categoryId()).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Category not found"
    ));
    Optional<City> optionalCity = cityRepository.findByName(input.cityName());

    City city = createOrgetCity(input, optionalCity);

    advertisement.setTitle(input.title());
    advertisement.setDescription(input.description());
    advertisement.setCategory(category);
    advertisement.setCity(city);

    return advertisementRepository.save(advertisement);
  }

  public void deleteAdvertisement(String id) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Advertisement not found"
    ));

    advertisementRepository.delete(advertisement);
  }

  public Advertisement addPhotoToAdvertisement(String id, MultipartFile image) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Advertisement not found"
    ));

    try {
      AdvertisementImage newAdvertisementImage = AdvertisementImage.builder()
          .advertisementId(advertisement.getId())
          .image(image.getBytes())
          .build();

      advertisementImageRepository.save(newAdvertisementImage);
    } catch (IOException e) {
      throw new RuntimeException();
    }

    return advertisementRepository.findById(id).orElseThrow(() -> new ApiException(
        HttpStatus.NOT_FOUND,
        "Advertisement not found"
    ));
  }
}

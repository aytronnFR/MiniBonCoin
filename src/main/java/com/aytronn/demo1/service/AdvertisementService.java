package com.aytronn.demo1.service;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dao.AdvertisementImage;
import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dao.City;
import com.aytronn.demo1.dto.AdvertisementInput;
import com.aytronn.demo1.dto.AdvertisementOutput;
import com.aytronn.demo1.mapper.AdvertisementMapper;
import com.aytronn.demo1.repository.AdvertisementImageRepository;
import com.aytronn.demo1.repository.AdvertisementRepository;
import com.aytronn.demo1.repository.CategoryRepository;
import com.aytronn.demo1.repository.CityRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    Category category = categoryRepository.findById(input.categoryId()).orElseThrow();

    Optional<City> optionalCity = cityRepository.findByName(input.cityName());

    City city = createOrgetCity(input, optionalCity);

    Advertisement newAdvertisement = Advertisement.builder()
        .title(input.title())
        .description(input.description())
        .category(category)
        .city(city)
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

  public List<Advertisement> getAllAdvertisement() {
    return advertisementRepository.findAll();
  }

  public AdvertisementOutput getAdvertisementById(String id) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow();

    return advertisementMapper.toAdvertisementOutput(advertisement);
  }

  public Advertisement updateAdvertisement(String id, AdvertisementInput input) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow();

    Category category = categoryRepository.findById(input.categoryId()).orElseThrow();
    Optional<City> optionalCity = cityRepository.findByName(input.cityName());

    City city = createOrgetCity(input, optionalCity);

    advertisement.setTitle(input.title());
    advertisement.setDescription(input.description());
    advertisement.setCategory(category);
    advertisement.setCity(city);

    return advertisementRepository.save(advertisement);
  }

  public void deleteAdvertisement(String id) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow();

    advertisementRepository.delete(advertisement);
  }

  public List<Advertisement> getAdvertisementByCategory(String categoryName) {
    return advertisementRepository.findByCategory_Name(categoryName);
  }

  public List<Advertisement> getAdvertisementByCity(String cityName) {
    return advertisementRepository.findByCity_Name(cityName);
  }

  public List<Advertisement> getAdvertisementByTitleContain(String titleContain) {
    return advertisementRepository.findByTitleContaining(titleContain);
  }

  public Advertisement addPhotoToAdvertisement(String id, MultipartFile image) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow();

    try {
      AdvertisementImage newAdvertisementImage = AdvertisementImage.builder()
          .advertisementId(advertisement.getId())
          .image(image.getBytes())
          .build();

      advertisementImageRepository.save(newAdvertisementImage);
    } catch (IOException e) {
      throw new RuntimeException();
    }

    return advertisementRepository.findById(id).orElseThrow();
  }
}

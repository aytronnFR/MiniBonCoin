package com.aytronn.demo1.service;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dao.AdvertisementImage;
import com.aytronn.demo1.dao.Category;
import com.aytronn.demo1.dto.AdvertisementInput;
import com.aytronn.demo1.repository.AdvertisementImageRepository;
import com.aytronn.demo1.repository.AdvertisementRepository;
import com.aytronn.demo1.repository.CategoryRepository;
import java.io.IOException;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdvertisementService {

  private final AdvertisementRepository advertisementRepository;
  private final CategoryRepository categoryRepository;
  private final AdvertisementImageRepository advertisementImageRepository;

  public AdvertisementService(AdvertisementRepository advertisementRepository, CategoryRepository categoryRepository,
      AdvertisementImageRepository advertisementImageRepository) {
    this.advertisementRepository = advertisementRepository;
    this.categoryRepository = categoryRepository;
    this.advertisementImageRepository = advertisementImageRepository;
  }

  public Advertisement createAdvertisement(AdvertisementInput input) {
    Category category = categoryRepository.findById(input.categoryId()).orElseThrow();

    Advertisement newAdvertisement = Advertisement.builder()
        .title(input.title())
        .description(input.description())
        .category(category)
        .build();

    return advertisementRepository.save(newAdvertisement);
  }

  public List<Advertisement> getAllAdvertisement() {
    return advertisementRepository.findAll();
  }

  public Advertisement getAdvertisementById(String id) {
    return advertisementRepository.findById(id).orElseThrow();
  }

  public Advertisement updateAdvertisement(String id, AdvertisementInput input) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow();

    Category category = categoryRepository.findById(input.categoryId()).orElseThrow();

    advertisement.setTitle(input.title());
    advertisement.setDescription(input.description());
    advertisement.setCategory(category);

    return advertisementRepository.save(advertisement);
  }

  public void deleteAdvertisement(String id) {
    Advertisement advertisement = advertisementRepository.findById(id).orElseThrow();

    advertisementRepository.delete(advertisement);
  }

  public List<Advertisement> getAdvertisementByCategory(String categoryId) {
    Category category = categoryRepository.findById(categoryId).orElseThrow();

    return advertisementRepository.findByCategory(category);
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

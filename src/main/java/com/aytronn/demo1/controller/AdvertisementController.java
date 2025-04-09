package com.aytronn.demo1.controller;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dto.AdvertisementInput;
import com.aytronn.demo1.dto.AdvertisementOutput;
import com.aytronn.demo1.service.AdvertisementService;
import java.util.List;
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
  public List<Advertisement> getAllAdvertisements(
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String cityName,
      @RequestParam(required = false) String titleContain
  ) {
    if (categoryId != null) {
      return advertisementService.getAdvertisementByCategory(categoryId);
    } else if (cityName != null) {
      return advertisementService.getAdvertisementByCity(cityName);
    } else if (titleContain != null) {
      return advertisementService.getAdvertisementByTitleContain(titleContain);
    } else {
      return advertisementService.getAllAdvertisement();
    }
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

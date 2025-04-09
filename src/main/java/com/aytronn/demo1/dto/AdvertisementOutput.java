package com.aytronn.demo1.dto;

import com.aytronn.demo1.dao.AdvertisementImage;
import java.util.List;
import lombok.Data;

@Data
public class AdvertisementOutput {
  private String title;
  private String description;
  private String categoryName;
  private List<AdvertisementImage> images;
  private String cityName;
}

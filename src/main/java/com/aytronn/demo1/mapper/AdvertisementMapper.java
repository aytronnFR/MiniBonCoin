package com.aytronn.demo1.mapper;

import com.aytronn.demo1.dao.Advertisement;
import com.aytronn.demo1.dto.AdvertisementOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

  @Mapping(target = "categoryName", source = "category.name")
  @Mapping(target = "cityName", source = "city.name")
  AdvertisementOutput toAdvertisementOutput(Advertisement advertisement);
}

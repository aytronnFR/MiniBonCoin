package com.aytronn.demo1.dto;

public record AdvertisementInput(
    String title,
    String description,
    String categoryId,
    String cityName
) {

}

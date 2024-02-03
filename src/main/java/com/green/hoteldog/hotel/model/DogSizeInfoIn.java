package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DogSizeInfoIn {
    @JsonProperty(value = "dog_size")
    private int dogSize;
    @JsonProperty(value = "dog_count")
    private int dogCount;
    private List<String> dates;
}

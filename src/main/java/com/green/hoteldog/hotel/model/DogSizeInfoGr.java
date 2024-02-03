package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DogSizeInfoGr {
    @JsonProperty(value = "all_dog_count")
    private int allDogCount;
    @JsonProperty(value = "biggest_dog_size")
    private int biggestDogSize;
    private List<LocalDate> date;
}

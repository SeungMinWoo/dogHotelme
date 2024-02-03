package com.green.hoteldog.hotel.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HotelRoomAbleListDto {
    @JsonProperty(value = "hotel_pk")
    private int hotelPk;
    @JsonProperty(value = "start_date")
    private String startDate;
    @JsonProperty(value = "end_date")
    private String endDate;
}


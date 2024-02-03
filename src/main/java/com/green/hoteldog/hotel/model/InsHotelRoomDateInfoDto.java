package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InsHotelRoomDateInfoDto {
    @JsonProperty(value = "hotel_room_pk")
    private int hotelRoomPk;
    @JsonProperty(value = "hotel_left_ea")
    private int hotelLeftEa;
    @JsonProperty(value = "room_date")
    private List<LocalDate> roomDate;
}

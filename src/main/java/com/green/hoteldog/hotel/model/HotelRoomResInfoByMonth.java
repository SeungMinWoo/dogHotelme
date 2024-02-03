package com.green.hoteldog.hotel.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HotelRoomResInfoByMonth {
    @JsonProperty(value = "hotel_room_nm")
    private String hotelRoomNm;
    @JsonProperty(value = "room_left_ea")
    private int roomLeftEa;
    @JsonProperty(value = "room_date")
    private String roomDate;
}


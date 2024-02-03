package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InsHotelRoomDto {
    @JsonIgnore
    private int hotelRoomPk;
    @JsonIgnore
    private int userPk;
    @JsonProperty(value = "hotel_pk")
    private int hotelPk;
    @JsonProperty(value = "size_pk")
    private int sizePk;
    @JsonProperty(value = "hotel_room_nm")
    private String hotelRoomNm;
    @JsonProperty(value = "room_pic")
    private String roomPic;
    @JsonProperty(value = "hotel_room_ea")
    private int hotelRoomEa;
    @JsonProperty(value = "hotel_room_cost")
    private int hotelRoomCost;
    private int maximum;
}

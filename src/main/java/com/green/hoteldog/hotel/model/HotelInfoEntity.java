package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "호텔 상세페이지 전체화면")
public class HotelInfoEntity {
    @JsonProperty(value = "hotel_info_vo")
    private HotelInfoVo hotelInfoVo;
    @JsonProperty(value = "my_dog_list")
    private List<MyDog> myDogList;
    @JsonProperty(value = "room_ea_by_dates")
    private List<HotelRoomEaByDate> roomEaByDates;
}

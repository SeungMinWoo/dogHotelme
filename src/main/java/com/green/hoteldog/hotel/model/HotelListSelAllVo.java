package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelListSelAllVo {
    @JsonProperty(value = "hotel_advertise_list")
    @Schema(title = "광고 리스트")
    List<HotelListSelVo> hotelAdvertiseList = new ArrayList<>();
    @JsonProperty(value = "hotel_list")
    @Schema(title = "호텔 리스트")
    List<HotelListSelVo> hotelList = new ArrayList<>();
}

package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "호텔 방 정보")
public class HotelRoomInfoVo {
    @NotNull
    @JsonProperty(value = "hotel_room_pk")
    private int hotelRoomPk;
    @NotNull
    @JsonProperty(value = "hotel_room_nm")
    private String hotelRoomNm;
    @NotNull
    @JsonProperty(value = "hotel_room_ea")
    private int hotelRoomEa;
    @NotNull
    @JsonProperty(value = "hotel_room_cost")
    private String hotelRoomCost;
    private String pic;
    @NotNull
    private int maximum;
}


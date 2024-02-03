package com.green.hoteldog.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DogInfo {
    @JsonIgnore
    @Schema(title = "예약 강아지 pk")
    @JsonProperty(value = "res_dog_pk")
    private Integer resDogPk;
    @JsonProperty(value = "size_pk")
    @Schema(title = "강아지 사이즈 pk")
    private int sizePk;
    @JsonProperty(value = "dog_nm")
    @Schema(title = "강아지 이름")
    private String dogNm;
    @JsonProperty(value = "dog_age")
    @Schema(title = "강아지 나이")
    private int dogAge;
    @Schema(title = "특이사항 및 요구사항")
    private String information; // 특이사항 및 요구사항
    @JsonProperty(value = "hotel_room_pk")
    @Schema(title = "호텔 방 pk")
    private int hotelRoomPk;
}

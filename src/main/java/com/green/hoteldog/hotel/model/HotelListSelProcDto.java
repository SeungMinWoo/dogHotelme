package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HotelListSelProcDto {
    // 회원 첫화면 시 사이즈 pk, 주소 저장
    // 회원 첫화면 시 사이즈 pk, 주소 저장
    @JsonProperty(value = "dog_size_pks")
    private int dogSizePks;
    private String address;
}

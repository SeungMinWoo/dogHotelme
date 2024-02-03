package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HotelInsDto {
    @JsonIgnore
    private int hotelPk;
    @JsonProperty(value = "user_pk")
    private int userPk;
    @JsonProperty(value = "hotel_nm")
    private String hotelNm;
    @JsonProperty(value = "hotel_detail_info")
    private String hotelDetailInfo;
    @JsonProperty(value = "business_num")
    private String businessNum;
    @JsonProperty(value = "hotel_call")
    private String hotelCall;
    @JsonProperty(value = "hotel_option")
    private List<Integer> hotelOption;
    @JsonIgnore
    private List<String> pics;
    @JsonProperty(value = "address_dto")
    private HotelAddressDto addressDto;
}

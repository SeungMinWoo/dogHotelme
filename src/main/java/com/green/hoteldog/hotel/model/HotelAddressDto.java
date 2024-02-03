package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HotelAddressDto {
    @JsonIgnore
    private int hotelPk;
    @JsonProperty(value = "address_name")
    private String addressName;
    @JsonProperty(value = "region_1depth_name")
    private String region1DepthName;
    @JsonProperty(value = "region_2depth_name")
    private String region2DepthName;
    @JsonProperty(value = "region_3depth_name")
    private String region3DepthName;
    @JsonProperty(value = "zone_num")
    private String zoneNum;
    private String x;
    private String y;
    @JsonProperty(value = "detail_address")
    private String detailAddress;
}

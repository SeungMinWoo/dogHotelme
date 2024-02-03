package com.green.hoteldog.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserAddressEntity {
    @JsonIgnore
    private int userPk;
    private String addressName;
    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;
    private String zoneNum;
    private String x;
    private String y;
    private String detailAddress;
}

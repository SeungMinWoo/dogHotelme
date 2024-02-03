package com.green.hoteldog.user.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyReviewHotelRoonNmVo {
    @JsonProperty(value = "resPk")
    private int resPk;
    private String hotelRoomNm;
    ;

}

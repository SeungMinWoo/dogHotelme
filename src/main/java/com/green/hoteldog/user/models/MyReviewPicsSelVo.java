package com.green.hoteldog.user.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MyReviewPicsSelVo {
    @JsonProperty(value = "review_pk")
    private int reviewPk;
    private String pic;
}

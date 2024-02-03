package com.green.hoteldog.review.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReviewPicVo {
    @JsonProperty(value = "review_pk")
    private int reviewPk;
    private String pic;
}

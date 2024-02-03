package com.green.hoteldog.review.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ReviewInsPicsDto {
    @JsonProperty(value = "review_pk")
    private int reviewPk;
    private List<String> pics;
}

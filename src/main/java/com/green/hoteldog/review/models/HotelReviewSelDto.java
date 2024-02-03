package com.green.hoteldog.review.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelReviewSelDto {
    @JsonProperty(value = "hotel_pk")
    private int hotelPk;
    @JsonProperty(value = "start_idx")
    private int startIdx;
    @JsonProperty(value = "row_count")
    private int rowCount;
    @JsonIgnore
    private List<Integer> reviewPk = new ArrayList<>();
    public void setPage(int page){
        this.startIdx = (page - 1) * rowCount;
    }
}
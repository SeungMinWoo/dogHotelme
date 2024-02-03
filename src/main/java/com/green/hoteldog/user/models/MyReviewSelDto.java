package com.green.hoteldog.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyReviewSelDto {

    private Integer userPk  ;
    @JsonIgnore
    private List<Integer> reviewPk = new ArrayList<>();
}

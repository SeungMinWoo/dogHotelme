package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MyDog {
    @JsonIgnore
    private int userDogPk;
    @JsonProperty(value = "size_pk")
    private int sizePk;
    @JsonProperty(value = "dog_nm")
    private String dogNm;
    @JsonProperty(value = "dog_pic")
    private String dogPic;
}


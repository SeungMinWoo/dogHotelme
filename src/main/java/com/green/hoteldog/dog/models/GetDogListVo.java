package com.green.hoteldog.dog.models;

import lombok.Data;

@Data
public class GetDogListVo {
    private int userDogPk;
    private int sizePk;
    private String dogSize;
    private String dogNm;
    private int dogAge;
    private String dogPic;
    private String dogEtc;
    private String createdAt;
}

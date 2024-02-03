package com.green.hoteldog.dog.models;

import lombok.Data;

@Data
public class SetUserDogPicDto {
    private int userPk;
    private int userDogPk;
    private String pic;
}

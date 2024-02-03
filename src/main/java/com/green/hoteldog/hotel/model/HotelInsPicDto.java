package com.green.hoteldog.hotel.model;

import lombok.Data;

import java.util.List;

@Data
public class HotelInsPicDto {
    private int userPk;
    private int hotelPk;
    private List<String> pics;
}

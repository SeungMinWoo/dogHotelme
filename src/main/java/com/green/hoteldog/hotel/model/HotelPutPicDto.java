package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class HotelPutPicDto {
    @JsonIgnore
    private int userPk;
    private int hotelPk;
    @JsonIgnore
    private List<MultipartFile> pics;
}

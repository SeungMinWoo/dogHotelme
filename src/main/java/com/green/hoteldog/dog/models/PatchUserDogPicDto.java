package com.green.hoteldog.dog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PatchUserDogPicDto {
    @JsonIgnore
    private int userPk;
    @JsonIgnore
    private MultipartFile pic;
    @Schema(description = "유저강아지 pk")
    private int userDogPk;
}

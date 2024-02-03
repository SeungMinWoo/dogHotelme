package com.green.hoteldog.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyReviewSelVo {
    private int reviewPk;
    private int resPk;
    private String hotelNm;
    // private String hotelRoomNm;
    private String comment;
    private String updatedAt;
    @Schema(title = "사진 3장")
    private List<String> pics = new ArrayList<>();
    private List<String> hotelRoomNm = new ArrayList<>();



}

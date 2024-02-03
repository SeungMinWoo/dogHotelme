package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "유저별 호텔 리뷰")
public class HotelReviewVo {
    @NotNull
    @JsonIgnore
    private int hotelPk;
    @NotNull
    @JsonIgnore
    private int userPk;
    @NotNull
    @JsonProperty(value = "nick_name")
    @Schema(name = "리뷰 작성한 유저 닉네임",type = "String")
    private String nickName;
    @Schema(name = "유저가 작성한 호텔 리뷰 글",type = "String")
    private String comment;
    @NotNull
    @Schema(name = "호텔 리뷰 별점",type = "int",description = "1~10")
    private int score;
    @JsonProperty(value = "created_at")
    @Schema(name = "리뷰 작성 시점")
    private String createdAt;
    @Schema(name = "리뷰 사진들",type = "List<String>")
    private List<String> pics;
    @Schema(name = "작성한 리뷰의 좋아요 갯수")
    @JsonProperty(value = "review_fav_count")
    private int reviewFavCount=0;
}


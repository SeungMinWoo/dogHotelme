package com.green.hoteldog.review.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(title = "호텔 리뷰 페이지네이션 VO")
public class HotelReviewSelVo {
    @JsonProperty(value = "review_pk")
    @Schema(title = "리뷰pk")
    private int reviewPk;
    @JsonProperty(value = "nick_name")
    @Schema(title = "닉네임")
    private String nickName;
    @Schema(title = "코멘트")
    private String comment;
    @Schema(title = "별점 (1~10)")
    private int score;
    @JsonProperty(value = "updated_at")
    @Schema(title = "리뷰 작성일")
    private String updatedAt;
    @JsonProperty(value = "fav_count")
    @Schema(title = "좋아요 수")
    private int favCount;
    @Schema(title = "사진 3장")
    private List<String> pics = new ArrayList<>(); // 사진
}

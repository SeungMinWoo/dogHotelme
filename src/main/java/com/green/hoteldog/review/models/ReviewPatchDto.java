package com.green.hoteldog.review.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReviewPatchDto {
    @JsonIgnore
    private int userPk;
    @Schema(description = "리뷰 pk")
    private int reviewPk;
    @Schema(description = "예약 pk")
    private int resPk;
    @Schema(description = "리뷰 코멘트")
    private String comment;
}

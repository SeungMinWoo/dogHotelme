package com.green.hoteldog.review.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewUpdDto {
    @JsonIgnore
    private int userPk;
    @JsonIgnore
    private List<MultipartFile> pics;
    @Min(value = 1,message = "입력값은 최소 1 이어야 합니다.")
    @Schema(description = "리뷰 pk")
    private int reviewPk;
    @Schema(description = "예약 pk")
    private int resPk;
    @Schema(description = "리뷰 코멘트")
    private String comment;
    @Min(value = 1,message = "평점은 최소 1 최대 10의 값을 보내야 합니다")
    @Max(value = 10,message = "평점은 최소 1 최대 10의 값을 보내야 합니다")
    @Schema(description = "리뷰 : 평정 1~10점까지 가능")
    private int score;
}

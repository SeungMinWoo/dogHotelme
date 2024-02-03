package com.green.hoteldog.board.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostCommentDto {
    @JsonIgnore
    private int userPk;
    @Min(value = 1,message = "boardPk값은 최소 1 이어야 합니다.")
    @Schema(description = "게시글pk",defaultValue = "1")
    private int boardPk;
    @NotEmpty(message = "comment값은 필수 입력값 입니다.")
    @Schema(description = "댓글 내용")
    private String comment;
}

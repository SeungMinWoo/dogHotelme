package com.green.hoteldog.board.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentInfoVo {
    @Schema(description = "댓글 pk")
    private int commentPk;
    @Schema(description = "댓글 작성 유저 pk")
    private int userPk;
    @Schema(description = "댓글 작성 유저 닉네임")
    private String userNickname;
    @Schema(description = "댓글 내용")
    private String comment;
    @Schema(description = "작성 시간")
    private String createdAt;
}

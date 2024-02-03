package com.green.hoteldog.board.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SimpleBoardVo {
    @Schema(description = "게시글 pk")
    private int boardPk;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 카테고리 이름")
    private String categoryNm;
    @Schema(description = "게시글 카테고리 pk")
    private int boardCategoryPk;
    @Schema(description = "게시글 작성자 pk")
    private int userPk;
    @Schema(description = "게시글 작성자 닉네임")
    private String nickname;
    @Schema(description = "게시글 조회수")
    private int boardViewCount;
    @Schema(description = "게시글 작성 시간")
    private String createdAt;
}

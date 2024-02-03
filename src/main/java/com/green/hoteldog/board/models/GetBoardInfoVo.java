package com.green.hoteldog.board.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GetBoardInfoVo {
    @Schema(description = "게시글 pk")
    private int boardPk;
    @Schema(description = "게시글 카테고리 pk")
    private int boardCategoryPk;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 작성자 pk")
    private int userPk;
    @Schema(description = "게시글 내용")
    private String contents;
    @Schema(description = "게시글 작성자 닉네임")
    private String nickname;
    @Schema(description = "작성 시간")
    private String createdAt;
    @Schema(description = "조회수")
    private int boardViewCount;
    @Schema(description = "사진")
    private List<String> pics;
    @Schema(description = "최대 페이지 수")
    private int commentMaxPage;
    @Schema(description = "댓글 개수")
    private int commentCount;
    @Schema(description = "댓글 정보")
    private List<CommentInfoVo> comments;
}

package com.green.hoteldog.board.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GetUserCommentVo {
    @Schema(description = "최대 페이지")
    private int maxPage;
    @Schema(description = "유저가 작성한 댓글 정보")
    private List<UserCommentVo> userCommentVoList;
}

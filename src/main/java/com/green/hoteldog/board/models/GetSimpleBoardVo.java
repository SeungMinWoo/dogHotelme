package com.green.hoteldog.board.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GetSimpleBoardVo {
    @Schema(description = "유저가 작성한 게시글 최대 페이지")
    private int maxPage;
    @Schema(description = "유저가 작성한 게시글 리스트")
    private List<SimpleBoardVo> simpleBoardVoList;
}

package com.green.hoteldog.board.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.hoteldog.common.Const;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetUserCommentListDto {
    @JsonIgnore
    private int userPk;
    @Schema(description = "유저가 작성한 댓글 페이지 번호",defaultValue = "1")
    private int page;
    @JsonIgnore
    private int rowCount = Const.BOARD_COUNT_PER_PAGE;
    @JsonIgnore
    private int startIdx;

    public void setPage(int page){
        if(page == 0){
            page = 1;
        }
        this.startIdx = (page - 1) * this.rowCount;
        this.page = page;
    }
}

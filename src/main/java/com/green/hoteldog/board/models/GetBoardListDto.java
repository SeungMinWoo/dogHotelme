package com.green.hoteldog.board.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.hoteldog.common.Const;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GetBoardListDto {
    @Min(value = 0,message = "boardCategoryPk 값은 0 이상이어야 합니다.")
    @Schema(description = "게시글 카테고리 pk",defaultValue = "0")
    private int boardCategoryPk;
    @Schema(description = "게시글 페이지 번호",defaultValue = "1")
    private int page;
    @JsonIgnore
    private int rowCount = Const.BOARD_COUNT_PER_PAGE;
    @Size(min = 2,message = "검색은 최소 2글자를 입력해야 합니다.")
    @Schema(description = "게시글 검색 값",defaultValue = "")
    private String search;
    @Min(value = 0,message = "searchType 값은 0 이상이어야 합니다.")
    @Schema(description = "게시글 검색 타입" , defaultValue = "1")
    private int searchType;
    @JsonIgnore
    private int startIdx;

    public void setPage(int page){
        if(page == 0){
            page = 1;
        }
        this.startIdx = (page - 1) * this.rowCount;
    }
}

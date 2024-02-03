package com.green.hoteldog.board.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BoardFavDto {
    @JsonIgnore
    private int userPk;
    @Min(value = 1,message = "boardPk 값은 최소 1 이어야 합니다.")
    @Schema(description = "게시글pk값", defaultValue = "0")
    private int boardPk;
}

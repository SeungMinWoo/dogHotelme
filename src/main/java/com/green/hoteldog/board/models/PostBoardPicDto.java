package com.green.hoteldog.board.models;

import lombok.Data;

import java.util.List;

@Data
public class PostBoardPicDto {
    private int boardPk;
    private List<String> pics;
}

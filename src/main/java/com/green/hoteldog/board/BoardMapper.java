package com.green.hoteldog.board;

import com.green.hoteldog.board.models.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int postBoard(PostBoardDto dto);
    int postBoardPics(PostBoardPicDto dto);
    int delBoardPics(int boardPk);
    int putBoard(PutBoardDto dto);
    /*
    임시중단
    int postFav(BoardFavDto dto);
    int delFav(BoardFavDto dto);
    */
    int delBoard(DeleteBoardDto dto);
    int postComment(PostCommentDto dto);
    int delComment(DeleteCommentDto dto);
    List<SimpleBoardVo> getBoardList(GetBoardListDto dto);
    GetBoardInfoVo getBoardInfo(int boardPk);
    List<String> selBoardPics(int boardPk);
    int boardViewCount(int boardPk);
    List<CommentInfoVo> selBoardComment(GetBoardInfoDto dto);
    int updComment(PutCommentDto dto);
    List<SimpleBoardVo> myPostingBoardList(GetUserBoardListDto dto);
    List<UserCommentVo> myPostingCommentList(GetUserCommentListDto dto);
    //총 게시글 개수
    int selBoardCount(GetBoardListDto dto);
    //게시글에 등록된 총 댓글 개수
    int selBoardCommentCount(int boardPk);
    //유저가 작성한 총 게시글 수
    int selUserBoardCount(int userPk);
    //유저가 작성한 총 댓글 수
    int selUserCommentCount(int userPk);
}

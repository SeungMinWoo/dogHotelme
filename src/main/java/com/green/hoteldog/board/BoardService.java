package com.green.hoteldog.board;

import com.green.hoteldog.board.models.*;
import com.green.hoteldog.common.Const;
import com.green.hoteldog.common.MyFileUtils;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.AuthorizedErrorCode;
import com.green.hoteldog.exceptions.BoardErrorCode;
import com.green.hoteldog.exceptions.CommonErrorCode;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardMapper mapper;
    private final MyFileUtils fileUtils;
    private final AuthenticationFacade facade;
    //게시글 등록 2024-01-18수정
    public ResVo postBoard(PostBoardDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        log.info("postDto : {}",dto);
        try {
            mapper.postBoard(dto);
        }catch (Exception e){
            return new ResVo(0);
        }
        if(dto.getPics() != null){
            List<String> pics = new ArrayList<>();
            String target = "/board/"+dto.getBoardPk();
            for(MultipartFile file : dto.getPics()){
                String saveFileNm = fileUtils.transferTo(file,target);
                pics.add(saveFileNm);
            }
            PostBoardPicDto picsDto = new PostBoardPicDto();
            picsDto.setBoardPk(dto.getBoardPk());
            picsDto.setPics(pics);
            try {
                mapper.postBoardPics(picsDto);
            }catch (Exception e){
                return new ResVo(0);
            }
        }
        return new ResVo(1);
    }
    //게시글 등록 2024-01-18수정

    //게시글 수정
    @Transactional(rollbackFor = Exception.class)
    public ResVo putBoard(PutBoardDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        int result = mapper.putBoard(dto);
        if (result == 0){
            return new ResVo(0);
        }
        if(dto.getPics() != null){
            String target = "/board/"+dto.getBoardPk();
            try {
                fileUtils.delFolderTrigger(target);
                mapper.delBoardPics(dto.getBoardPk());
            }catch (Exception e){
                throw new CustomException(CommonErrorCode.ACCEPTED);
            }
            List<String> pics = new ArrayList<>();
            for(MultipartFile file : dto.getPics()){
                String saveFileNm = fileUtils.transferTo(file,target);
                pics.add(saveFileNm);
            }

            PostBoardPicDto picsDto = new PostBoardPicDto();
            picsDto.setBoardPk(dto.getBoardPk());
            picsDto.setPics(pics);
            try {
                mapper.postBoardPics(picsDto);
            }catch (Exception e){
                throw new CustomException(CommonErrorCode.ACCEPTED);
            }
        }
        return new ResVo(1);


    }
    //게시글 수정

    /*//게시글 좋아요 임시 중단
    public ResVo putBoardFav(BoardFavDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            //예외처리 로그인 하지 않은 유저는 좋아요 할 수 없음
        }
        if(mapper.delFav(dto) == 0){
            mapper.postFav(dto);
            return new ResVo(1);
        }
        return new ResVo(2);
    }
    //게시글 좋아요*/

    //게시글 삭제
    @Transactional(rollbackFor = Exception.class)
    public ResVo deleteBoard(DeleteBoardDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        log.info("DeleteBoardDto : {}",dto);
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        int result = mapper.delBoard(dto);
        log.info("result : {} , dto.getBoardPkList().size() : {}",result,dto.getBoardPkList().size());
        if (result != dto.getBoardPkList().size()){
            throw new CustomException(BoardErrorCode.BAD_REQUEST_BOARD_PK);
        }

        for(Integer boardPk : dto.getBoardPkList()){
            String target = "/board/"+boardPk;
            fileUtils.delAllFolderTrigger(target);
        }
        return new ResVo(Const.SUCCESS);
    }
    //게시글 삭제

    //댓글 등록
    public ResVo postComment(PostCommentDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        int result = mapper.postComment(dto);
        return new ResVo(result);
    }
    //댓글 등록

    //댓글 삭제
    @Transactional(rollbackFor = Exception.class)
    public ResVo deleteComment(DeleteCommentDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        int result = mapper.delComment(dto);
        if (result != dto.getCommentPkList().size()){
            throw new CustomException(BoardErrorCode.BAD_REQUEST_BOARD_PK);
        }
        return new ResVo(Const.SUCCESS);
    }
    //댓글 삭제

    //댓글 수정
    public ResVo updateComment(PutCommentDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        int result = mapper.updComment(dto);
        return new ResVo(result);
    }
    //댓글 수정

    //게시글 리스트
    public GetSimpleBoardVo getBoardList(GetBoardListDto dto){
        GetSimpleBoardVo vo = new GetSimpleBoardVo();
        vo.setSimpleBoardVoList(mapper.getBoardList(dto));
        int boardCount = mapper.selBoardCount(dto);
        int maxPage = 1;
        if (boardCount != 0){
            maxPage = this.maxPage(boardCount,dto.getRowCount());
        }
        vo.setMaxPage(maxPage);

        return vo;
    }
    //게시글 리스트

    //게시글 정보
    public GetBoardInfoVo getBoardInfo(GetBoardInfoDto dto){
        mapper.boardViewCount(dto.getBoardPk());
        GetBoardInfoVo vo = mapper.getBoardInfo(dto.getBoardPk());
        vo.setPics(mapper.selBoardPics(dto.getBoardPk()));
        vo.setComments(mapper.selBoardComment(dto));
        int commentCount = mapper.selBoardCommentCount(dto.getBoardPk());
        vo.setCommentCount(commentCount);
        int commentMaxPage = this.maxPage(commentCount,dto.getRowCount());
        if(commentMaxPage == 0){
            commentMaxPage = 1;
        }
        vo.setCommentMaxPage(commentMaxPage);
        return vo;
    }
    //게시글 정보

    //로그인 유저가 작성한 게시글
    public GetSimpleBoardVo userPostingBoardList(GetUserBoardListDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        GetSimpleBoardVo vo = new GetSimpleBoardVo();
        vo.setSimpleBoardVoList(mapper.myPostingBoardList(dto));
        int userBoardCount = mapper.selUserBoardCount(dto.getUserPk());
        int userBoardMaxPage = 1;
        if(userBoardCount != 0){
            userBoardMaxPage = this.maxPage(userBoardCount,dto.getRowCount());
        }
        vo.setMaxPage(userBoardMaxPage);
        return vo;
    }
    //로그인 유저가 작성한 게시글

    //로그인 유저가 작성한 댓글
    public GetUserCommentVo userPostingCommentList(GetUserCommentListDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        GetUserCommentVo vo = new GetUserCommentVo();
        vo.setUserCommentVoList(mapper.myPostingCommentList(dto));
        int userCommentCount = mapper.selUserCommentCount(dto.getUserPk());
        int userCommentMaxPage = 1;
        if(userCommentCount != 0){
            userCommentMaxPage = this.maxPage(mapper.selUserCommentCount(dto.getUserPk()),dto.getRowCount());
        }
        vo.setMaxPage(userCommentMaxPage);
        return vo;
    }
    //로그인 유저가 작성한 댓글



    //총 페이지 수 계산
    public int maxPage (int columnCount,int rowCount){
        return (int)Math.ceil((double) columnCount / rowCount);
    }

}

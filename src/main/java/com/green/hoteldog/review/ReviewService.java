package com.green.hoteldog.review;

import com.green.hoteldog.common.Const;
import com.green.hoteldog.common.MyFileUtils;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.*;
import com.green.hoteldog.review.models.*;
import com.green.hoteldog.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper mapper;
    private final AuthenticationFacade facade;
    private final MyFileUtils fileUtils;


    //-----------------------------------------------------리뷰 등록------------------------------------------------------
    private void checkResUser(int resPk, int userPk) {
        CheckResUserDto checkResUserDto = new CheckResUserDto();
        checkResUserDto.setResPk(resPk);
        checkResUserDto.setUserPk(userPk);
        if (mapper.checkResUser(checkResUserDto) == null || mapper.checkResUser(checkResUserDto) != userPk) {
            throw new CustomException(ReviewErrorCode.MIS_MATCH_USER_PK);
        }
    }

    public ResVo insReview(ReviewInsDto dto) {
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0) {
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        int checkStatus = mapper.checkResStatus(dto);
        if (checkStatus == 0) {
            throw new CustomException(ReviewErrorCode.NOT_CHECK_OUT_STATUS);
        }
        try {
            mapper.insReview(dto);
        } catch (Exception e) {
            return new ResVo(0);
        }
        if (dto.getPics() != null) {
            List<String> pics = new ArrayList<>();
            String target = "/review/" + dto.getReviewPk();
            fileUtils.delFiles(target);
            for (MultipartFile file : dto.getPics()) {
                String saveFileNm = fileUtils.transferTo(file, target);
                pics.add(saveFileNm);
            }
            ReviewInsPicsDto picsDto = new ReviewInsPicsDto();
            picsDto.setReviewPk(dto.getReviewPk());
            picsDto.setPics(pics);
            mapper.insReviewPics(picsDto);
        }
        return new ResVo(1);
    }
    //--------------------------------------------------리뷰 전체 수정-----------------------------------------------------

    @Transactional(rollbackFor = Exception.class)
    public ResVo putReview(ReviewUpdDto dto) {
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0) {
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        checkResUser(dto.getResPk(), dto.getUserPk());
        try {
            mapper.updReview(dto);
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }
        if (dto.getPics() != null) {
            mapper.delReviewPics(dto);
            ReviewInsPicsDto picsDto = new ReviewInsPicsDto();
            List<String> pics = new ArrayList<>();
            String target = "/review/" + dto.getReviewPk();
            fileUtils.delFolderTrigger(target);
            for (MultipartFile file : dto.getPics()) {
                String saveFileNm = fileUtils.transferTo(file, target);
                pics.add(saveFileNm);
            }
            picsDto.setReviewPk(dto.getReviewPk());
            picsDto.setPics(pics);
            mapper.insReviewPics(picsDto);
        }
        return new ResVo(1);

    }
    //--------------------------------------------------리뷰 코멘트 수정---------------------------------------------------

    public ResVo patchReviewComment(ReviewPatchDto dto) {
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0) {
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }

        checkResUser(dto.getResPk(), dto.getUserPk());
        try {
            mapper.updReviewComment(dto);
            return new ResVo(1);
        } catch (Exception e) {
            return new ResVo(0);
        }
    }

    //--------------------------------------------------리뷰 좋아요 토글---------------------------------------------------
    public ResVo patchReviewFav(ReviewFavDto dto) {
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0) {
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        if (mapper.delReviewFav(dto) == 0) {
            int result = mapper.insReviewFav(dto);
            return new ResVo(result);
        }
        return new ResVo(2);
    }

    //--------------------------------------------------리뷰 삭제---------------------------------------------------

    @Transactional(rollbackFor = Exception.class)
    public ResVo delReview(DelReviewDto dto) {
        dto.setUserPk(facade.getLoginUserPk());
        log.info("DelReviewDto : {}", dto);
        if (dto.getUserPk() == 0) {
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        checkResUser(dto.getResPk(), dto.getUserPk());
        try {
            mapper.delReviewFavAll(dto);
            mapper.delReviewPicsAll(dto);
            mapper.delReview(dto);
            return new ResVo(Const.SUCCESS);
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.CONFLICT);
        }

    }

    //------------------------------------------------호텔 리뷰-----------------------------------------------------------
    public List<HotelReviewSelVo> getHotelReview(HotelReviewSelDto dto) {
        // n+1 이슈 해결
        try {
            List<HotelReviewSelVo> list = mapper.selHotelReview(dto);
            for (HotelReviewSelVo vo : list) {
                dto.getReviewPk().add(vo.getReviewPk());
            }
            List<HotelReviewPicsSelVo> pics = mapper.selHotelReviewPics(dto);

            // pk를 담을 list, pk 및 해당 객체 주솟 값을 담을 map 생성
            List<Integer> revPk = new ArrayList<>();
            Map<Integer, HotelReviewSelVo> hashMap = new HashMap<>();
            for (HotelReviewSelVo vo : list) {
                revPk.add(vo.getReviewPk());
                hashMap.put(vo.getReviewPk(), vo);
            }
            for (HotelReviewPicsSelVo vo : pics) {
                hashMap.get(vo.getReviewPk()).getPics().add(vo.getPic());
            }
            // 사진 3개까지 제거
            for (HotelReviewSelVo vo : list) {
                while (vo.getPics().size() > 3) {
                    vo.getPics().remove(vo.getPics().size() - 1);
                }
            }
            return list;
        } catch (Exception e) {
            throw new CustomException(ReviewErrorCode.PAGE_COUNT_EXEEDED_ERROR);
        }
    }
}


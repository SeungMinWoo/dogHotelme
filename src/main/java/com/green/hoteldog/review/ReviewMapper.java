package com.green.hoteldog.review;

import com.green.hoteldog.review.models.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int checkResStatus(ReviewInsDto dto);
    int insReview(ReviewInsDto dto);
    int insReviewPics(ReviewInsPicsDto dto);
    int updReview(ReviewUpdDto dto);
    int delReviewPics(ReviewUpdDto dto);
    int updReviewComment(ReviewPatchDto dto);
    int delReviewFav(ReviewFavDto dto);
    int insReviewFav(ReviewFavDto dto);
    List<HotelReviewSelVo> selHotelReviewList(HotelReviewSelDto dto);
    List<ReviewPicVo> selReviewPics(List<Integer> reviewPkList);
    int delReview(DelReviewDto dto);
    List<HotelReviewSelVo> selHotelReview(HotelReviewSelDto dto);
    List<HotelReviewPicsSelVo> selHotelReviewPics(HotelReviewSelDto dto);
    Integer checkResUser(CheckResUserDto dto);
    int delReviewFavAll(DelReviewDto dto);
    int delReviewPicsAll(DelReviewDto dto);

}

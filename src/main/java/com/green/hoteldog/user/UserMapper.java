package com.green.hoteldog.user;

import com.green.hoteldog.user.models.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    int userSignup(UserSignupDto dto);
    int insUserAddress(UserAddressEntity entity);
    /*int insUserAddress(UserSignupDto dto);*/
    UserEntity userEntityByUserEmail(String userEmail);
    List<UserEntity> selUserEntity();
    List<Integer> selUserDogSize(int userPk);
    String selUserDepthName(int userPk);
    UserEntity userEntityByUserPk(int userPk);
    int updateUserInfo(UserUpdateDto dto);
    List<MyReviewSelVo> selMyReview(MyReviewSelDto dto);
    List<MyReviewHotelRoonNmVo>selMyReviewHotelRoonNm(MyReviewSelDto dto);
    List<MyReviewPicsSelVo> selMyReviewPic(MyReviewSelDto dto);




}

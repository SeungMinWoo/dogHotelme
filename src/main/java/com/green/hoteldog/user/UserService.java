package com.green.hoteldog.user;

import com.green.hoteldog.common.AppProperties;
import com.green.hoteldog.common.CookieUtils;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.AuthorizedErrorCode;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.exceptions.UserErrorCode;
import com.green.hoteldog.review.models.HotelReviewPicsSelVo;
import com.green.hoteldog.review.models.HotelReviewSelVo;
import com.green.hoteldog.user.models.MyReviewSelDto;
import com.green.hoteldog.user.models.MyReviewSelVo;
import com.green.hoteldog.security.AuthenticationFacade;
import com.green.hoteldog.security.JwtTokenProvider;
import com.green.hoteldog.security.MyUserDtails;
import com.green.hoteldog.security.Myprincipal;
import com.green.hoteldog.user.models.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookie;
    private final AuthenticationFacade facade;

    //유저 회원가입
    @Transactional(rollbackFor = {Exception.class})
    public ResVo userSignup(UserSignupDto dto) {
        String pw = passwordEncoder.encode(dto.getUpw());
        dto.setUpw(pw);
        dto.setUserEmail(dto.getEmailResponseVo().getEmail());
        dto.setUserTypePk(1);
        String userAddress = dto.getAddressEntity().getAddressName() + " " + dto.getAddressEntity().getDetailAddress();
        dto.setUserAddress(userAddress);
        mapper.userSignup(dto);
        log.info("userPk : {}", dto.getUserPk());
        dto.getAddressEntity().setUserPk(dto.getUserPk());
        log.info("userAddressUserPk : {}", dto.getAddressEntity().getUserPk());
        mapper.insUserAddress(dto.getAddressEntity());
        return new ResVo(1);

    }

    //유저 로그인
    public UserSigninVo userSignin(HttpServletResponse response, HttpServletRequest request, UserSigninDto dto) {
        UserSigninVo vo = new UserSigninVo();
        UserEntity userEntity = mapper.userEntityByUserEmail(dto.getUserEmail());
        if (userEntity == null) {
            throw new CustomException(UserErrorCode.UNKNOWN_EMAIL_ADDRESS);
        }
        if (!passwordEncoder.matches(dto.getUpw(), userEntity.getUpw())) {
            throw new CustomException(UserErrorCode.MISS_MATCH_PASSWORD);
        }
        Myprincipal myprincipal = new Myprincipal(userEntity.getUserPk());
        String at = tokenProvider.generateAccessToken(myprincipal);
        //엑서스 토큰 값 받아오기
        String rt = tokenProvider.generateRefreshToken(myprincipal);
        //리프레쉬 토큰 값 받아오기
        /*List<Integer> dogSizeList = mapper.selUserDogSize(userEntity.getUserPk());*/

        int rtCookieMaxAge = (int) appProperties.getJwt().getRefreshTokenExpiry() / 1000;
        cookie.deleteCookie(response, "rt");
        cookie.setCookie(response, "rt", rt, rtCookieMaxAge);

        /*vo.setDepthName(mapper.selUserDepthName(userEntity.getUserPk()));
        vo.setSizePkList(dogSizeList);*/
        vo.setUserPk(userEntity.getUserPk());
        vo.setUserTypePk(userEntity.getUserTypePk());
        vo.setAccessToken(at);
        vo.setNickname(userEntity.getNickname());
        return vo;
    }

    public ResVo checkNickname(String nickname) {
        List<UserEntity> userEntityList = mapper.selUserEntity();
        for (UserEntity entity : userEntityList) {
            if (entity.getNickname().equals(nickname)) {
                throw new CustomException(UserErrorCode.ALREADY_USED_NICKNAME);
            }
        }
        return new ResVo(1);
    }

    public ResVo signout(HttpServletResponse response) {
        cookie.deleteCookie(response, "rt");
        return new ResVo(1);
    }

    public UserInfoVo getUserInfo(UserInfoDto dto) {
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0) {
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        UserEntity entity = mapper.userEntityByUserPk(dto.getUserPk());
        if (!passwordEncoder.matches(dto.getUpw(), entity.getUpw())) {
            throw new CustomException(UserErrorCode.MISS_MATCH_PASSWORD);
        }
        UserInfoVo vo = new UserInfoVo();
        vo.setUserPk(entity.getUserPk());
        vo.setUserEmail(entity.getUserEmail());
        vo.setNickname(entity.getNickname());
        vo.setPhoneNum(entity.getPhoneNum());
        vo.setUserAddress(entity.getUserAddress());
        return vo;
    }

    public ResVo updUserInfo(UserUpdateDto dto) {
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0) {
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        try {
            mapper.updateUserInfo(dto);
            return new ResVo(1);
        } catch (Exception e) {
            return new ResVo(0);
        }
    }

    public RefreshTokenVo getRefreshToken(HttpServletRequest request) {
        RefreshTokenVo vo = new RefreshTokenVo();
        Cookie userCookie = cookie.getCookie(request, "rt");
        if (userCookie == null) {
            throw new CustomException(AuthorizedErrorCode.NOT_EXISTS_REFRESH_TOKEN);
        }
        String token = userCookie.getValue();
        if (!tokenProvider.isValidateToken(token)) {
            throw new CustomException(AuthorizedErrorCode.REFRESH_TOKEN_IS_EXPIRATION);
        }
        MyUserDtails myUserDtails = (MyUserDtails) tokenProvider.getUserDetailsFromToken(token);
        Myprincipal myprincipal = myUserDtails.getMyprincipal();
        String at = tokenProvider.generateAccessToken(myprincipal);
        vo.setUserPk(facade.getLoginUserPk());
        vo.setAccessToken(at);
        return vo;
    }

    public  List<MyReviewSelVo> selMyReview(MyReviewSelDto dto){
//        return mapper.selMyReview(dto);



        List<MyReviewSelVo> list = mapper.selMyReview(dto);
        for (MyReviewSelVo vo : list){
            dto.getReviewPk().add(vo.getReviewPk());
        }
        List<MyReviewPicsSelVo> pics =mapper.selMyReviewPic(dto);

        List<Integer> revPk = new ArrayList<>();
        Map<Integer,MyReviewSelVo> hasgMap = new HashMap<>();
        for (MyReviewSelVo vo : list) {
            revPk.add(vo.getReviewPk());
            hasgMap.put(vo.getReviewPk(),vo);
        }
        for (MyReviewPicsSelVo vo : pics){
            hasgMap.get(vo.getReviewPk()).getPics().add(vo.getPic());
        }
        for (MyReviewSelVo vo : list) {
            while (vo.getPics().size() > 3) {
                vo.getPics().remove(vo.getPics().size() - 1);
            }
        }
        return list;
    }
}
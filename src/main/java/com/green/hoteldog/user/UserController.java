package com.green.hoteldog.user;

import com.green.hoteldog.common.RedisUtil;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.exceptions.UserErrorCode;
import com.green.hoteldog.user.models.MyReviewSelDto;
import com.green.hoteldog.user.models.MyReviewSelVo;
import com.green.hoteldog.user.models.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 API",description = "유저 관련 처리")
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;
    private final RedisUtil redisUtil;

    @PostMapping("/signout")
    @Operation(summary = "유저 로그아웃",description = "로그아웃 처리 및 쿠키삭제")
    public ResVo postSignout(HttpServletResponse response){
        return service.signout(response);
    }

    //유저 회원가입
    @PostMapping("/signup")
    @Operation(summary = "유저 회원가입",description = "유저 회원가입 처리")
    public ResVo userSignup(@RequestBody @Valid UserSignupDto dto){

        ResVo vo = new ResVo(0);
        if(dto.getEmailResponseVo().getResult() == 0){
            throw new CustomException(UserErrorCode.NOT_CERTIFICATION_EMAIL);
        }
        if(dto.getEmailResponseVo().getResult() == 1){
            vo = service.userSignup(dto);
        }
        if(vo.getResult() == 1){
            redisUtil.deleteData(dto.getEmailResponseVo().getEmail());
        }
        return vo;
    }
    //유저 로그인
    @PostMapping("/signin")
    @Operation(summary = "유저 로그인",description = "유저 로그인 처리")
    public UserSigninVo userSignin(HttpServletResponse response,HttpServletRequest request, @RequestBody @Valid UserSigninDto dto){
        return service.userSignin(response,request,dto);
    }

    //닉네임 중복체크
    @GetMapping("/nickname-check")
    @Operation(summary = "닉네임 체크")
    public ResVo nicknameCheck(String nickname){
        return service.checkNickname(nickname);
    }
    //유저 정보 불러오기
    @PostMapping("/info")
    @Operation(summary = "회원정보 불러오기")
    public UserInfoVo userInfo (@RequestBody UserInfoDto dto){
        return service.getUserInfo(dto);
    }
    //유저 정보 수정
    @PutMapping("/info")
    @Operation(summary = "유저 정보수정")
    public ResVo updUserInfo (@RequestBody UserUpdateDto dto){
        return service.updUserInfo(dto);
    }
    //리프레쉬 토큰 재발급
    @GetMapping("/refresh-token")
    public RefreshTokenVo getRefreshToken (HttpServletRequest request){
        return service.getRefreshToken(request);
    }



    @GetMapping("/info")
    @Operation(summary = "이용 후기")
    public List<MyReviewSelVo> selMyReview(MyReviewSelDto dto){
        return service.selMyReview(dto);
    }
}
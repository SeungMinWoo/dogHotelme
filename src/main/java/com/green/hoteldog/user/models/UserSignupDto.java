package com.green.hoteldog.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.hoteldog.email.EmailResponseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupDto {
    @JsonIgnore
    private int userPk;
    @JsonIgnore
    private String userEmail;
    @JsonIgnore
    @Schema(description = "유저 타입",defaultValue = "1")
    private int userTypePk;
    private EmailResponseVo emailResponseVo;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",message = "비밀번호는 영문,숫자 조합으로 8글자 이상입니다.")
    @NotEmpty(message = "비밀번호를 입력해 주세요")
    @Schema(description = "유저 패스워드",defaultValue = "password")
    private String upw;
    @Size(min = 2,message = "닉네임은 최소 2글자 입니다.")
    private String nickname;
    @Pattern(regexp = "^01[0-1|6-9]{1}[\\d]{3,4}[\\d]{4}",message = "올바른 전화번호 입력이 아닙니다.")
    private String phoneNum;
    @JsonIgnore
    private String userAddress;


    private UserAddressEntity addressEntity;
}

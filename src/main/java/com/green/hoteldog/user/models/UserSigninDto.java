package com.green.hoteldog.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserSigninDto {
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@([a-zA-Z0-9]{3,}\\.[a-z]{2,3}|[a-zA-Z0-9]{3,}\\.[a-z]{2,3}\\.[a-z]{2,3})$",message = "올바른 이메일 형식이 아닙니다.")
    @NotEmpty(message = "아이디를 입력해 주세요")
    @Schema(description= "이메일",defaultValue = "email")
    private String userEmail;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    @NotEmpty(message = "비밀번호를 입력해 주세요")
    @Schema(description = "유저 패스워드",defaultValue = "password")
    private String upw;
}

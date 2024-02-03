package com.green.hoteldog.user.models;

import lombok.Data;

@Data
public class UserInfoVo {
    private int userPk;
    private String userEmail;
    private String nickname;
    private String phoneNum;
    private String userAddress;

}

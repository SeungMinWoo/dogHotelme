package com.green.hoteldog.user.models;

import lombok.Data;

@Data
public class UserEntity {
    private int userPk;
    private int userTypePk;
    private String userEmail;
    private String upw;
    private String nickname;
    private String phoneNum;
    private String userAddress;
}

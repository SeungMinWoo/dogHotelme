package com.green.hoteldog.user.models;

import lombok.Data;

@Data
public class RefreshTokenVo {
    private int userPk;
    private String accessToken;
}

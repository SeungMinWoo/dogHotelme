package com.green.hoteldog.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserUpdateDto {
    @JsonIgnore
    private int userPk;
    private String nickname;
    private String phoneNum;
    private String userAddress;

    private UserAddressEntity addressEntity;
}

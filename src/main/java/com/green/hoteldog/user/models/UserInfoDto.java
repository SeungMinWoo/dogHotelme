package com.green.hoteldog.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserInfoDto {
    @JsonIgnore
    private int userPk;
    private String upw;
}

package com.green.hoteldog.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Myprincipal {//토큰에 값을 넣기 위해 만들어짐
    private int userPk;
}

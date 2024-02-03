package com.green.hoteldog.email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailResponseVo {
    @Schema(description = "인증받은 이메일")
    private String email;
    @Schema(description = "성공 여부")
    private int result;
}

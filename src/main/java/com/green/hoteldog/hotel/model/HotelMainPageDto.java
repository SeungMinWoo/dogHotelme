package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(name = "호텔 상세페이지 요청데이터")
public class HotelMainPageDto {
    @NotNull
    @Schema(name = "호텔 pk",minimum = "0",description = "호텔 상세 페이지<br>" +
            "출력 시 필요한 호텔 pk")
    @JsonProperty(value = "hotel_pk")
    private int hotelPk;

    @Schema(name = "유저 pk",minimum = "0",description = "호텔 상세 페이지<br>" +
            "출력시 필요한 유저 pk<br>" +
            "로그인 안하고 상세페이지 들어올 시 유저pk=0 <br>" +
            "이외에는 로그인 시 유저 pk 필요.")
    @JsonProperty(value = "user_pk")
    private int userPk=0;

}

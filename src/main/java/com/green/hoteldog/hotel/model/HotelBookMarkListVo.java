package com.green.hoteldog.hotel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "북마크 등록 한 호텔 리스트")
public class HotelBookMarkListVo {
    @Schema(name = "호텔 이름")
    @JsonProperty(value = "hotel_nm")
    private String hotelNm;
    @Schema(name = "호텔 사진")
    @JsonProperty(value = "hotel_pic")
    private String hotelPic;
    @Schema(name = "호텔Pk")
    @JsonProperty(value = "hotel_pk")
    private int hotelPk;
}

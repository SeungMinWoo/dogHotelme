package com.green.hoteldog.reservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "호텔 예약 정보 (List)")
public class ResInfoVo {
    @Schema(name = "호텔이름")
    @JsonProperty(value = "hotel_nm")
    private String hotelNm;
    @Schema(name = "호텔 전화번호")
    @JsonProperty(value = "hotel_call")
    private String hotelCall;
    @Schema(name = "호텔 방 이름")
    @JsonProperty(value = "hotel_room_nm")
    private String hotelRoomNm;
    @Schema(name = "예약 시작날짜")
    @JsonProperty(value = "from_date")
    private String fromDate;
    @Schema(name = "예약 끝나는 날짜")
    @JsonProperty(value = "to_date")
    private String toDate;
    @Schema(name = "방 사진")
    @JsonProperty(value = "room_pic")
    private String roomPic;
    @Schema(name = "예약 Pk")
    @JsonProperty(value = "res_pk")
    private int resPk;
    @Schema(name = "예약 강아지 정보 리스트")
    @JsonProperty(value = "res_dog_info_vo_list")
    private List<ResDogInfoVo> resDogInfoVoList;
}

package com.green.hoteldog.reservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "예약 강아지 정보")
public class ResDogInfoVo {
    @Schema(name = "예약 Pk")
    @JsonProperty(value = "res_pk")
    private int resPk;
    @Schema(name = "예약 강아지 이름")
    @JsonProperty(value = "dot_nm")
    private String dogNm;
}

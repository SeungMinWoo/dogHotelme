package com.green.hoteldog.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Schema(title = "호텔 예약 삭제 DTO")
public class HotelReservationDelDto {
    @Schema(title = "예약 pk")
    private int resPk;
    @JsonIgnore
    private int userPk;
    @JsonIgnore
    private List<Integer> resDogPkList = new ArrayList<>();
    @JsonIgnore
    private List<LocalDate> dateRange;
    @JsonIgnore
    private List<Integer> hotelRoomPk;
}
package com.green.hoteldog.reservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelReservationSelProcVo {
    @JsonProperty(value = "res_pk")
    private Integer resPk;
    @JsonProperty(value = "from_date")
    private LocalDate fromDate;
    @JsonProperty(value = "to_date")
    private LocalDate toDate;
    @JsonProperty(value = "room_count")
    private int roomCount;
    @JsonProperty(value = "res_dog_pk")
    private int resDogPk;
}

package com.green.hoteldog.reservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HotelReservationUpdProcDto {
    @JsonProperty(value = "hotel_room_pk")
    private int hotelRoomPk;
    private List<LocalDate> date;
}

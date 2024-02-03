package com.green.hoteldog.reservation;

import com.green.hoteldog.reservation.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    //-------------------------------------------------호텔 예약----------------------------------------------------------
    int insHotelReservation(HotelReservationInsDto dto);
    int insHotelReservationDogInfo(DogInfo info);
    int insHotelReservationInfo(HotelReservationInsDto dto);
    int updRemainedHotelRoom(List<HotelReservationUpdProcDto> dto);
    //-------------------------------------------------예약 취소----------------------------------------------------------
    List<HotelReservationSelProcVo> selHotelReservation(HotelReservationDelDto dto);
    List<Integer> selHotelRoomPk(HotelReservationDelDto dto);
    int delHotelReservationInfo(HotelReservationDelDto dto);
    int delHotelReservationDogInfo(HotelReservationDelDto dto);
    int delHotelReservation(HotelReservationDelDto dto);
    int updRemainedHotelRoom2(HotelReservationUpdProc2Dto dto);
    //--------------------------------------------예약 정보 불러오기--------------------------------------------------------
    List<ResInfoVo> getUserReservation(int userPk,int perPage,int pages);
    List<ResDogInfoVo> getDogInfoReservation(List<Integer> resPk);
}
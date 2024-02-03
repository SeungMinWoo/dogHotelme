package com.green.hoteldog.reservation;

import com.green.hoteldog.common.Const;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.CommonErrorCode;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.exceptions.ReservationErrorCode;
import com.green.hoteldog.reservation.model.*;
import com.green.hoteldog.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    //--------------------------------------------------호텔 예약---------------------------------------------------------
    @Transactional(rollbackFor = Exception.class)
    public ResVo postHotelReservation(List<HotelReservationInsDto> dto){
        log.info("dto : {}", dto);
        for ( HotelReservationInsDto dtos : dto ) {
            dtos.setUserPk(authenticationFacade.getLoginUserPk());
            if(dtos.getUserPk() == 0){
                throw new CustomException(ReservationErrorCode.UNKNOWN_USER_PK);
            }
            int affectedrows = mapper.insHotelReservation(dtos);
            dtos.setResPk(dtos.getResPk());
            if (dtos.getResPk() == null){
                throw new CustomException(ReservationErrorCode.RESERVATION_TABLE_REGISTRATION_FAILED); // 예약 테이블에 등록 실패
            }
            for ( DogInfo info : dtos.getDogInfo() ) {
                int affectedRows1 = mapper.insHotelReservationDogInfo(info);
                if(affectedRows1 == 0){
                    throw new CustomException(ReservationErrorCode.RESERVATION_DOG_TABLE_REGISTRATION_FAILED); // 예약 강아지 정보 등록 실패
                }
            }
        }
        for ( HotelReservationInsDto dtos : dto ) {
            int affectedRows2 = mapper.insHotelReservationInfo(dtos);
            if(affectedRows2 == 0){
                throw new CustomException(ReservationErrorCode.ROOM_AND_DOG_RESERVATION_TABLE_REGISTRATION_FAILED); // 예약방강아지 예약정보 등록 실패
            }
        }
        // 호텔 방 관리 테이블 업데이트
        // 날짜 list , 호텔 방 pk list 박스 생성
        List<HotelReservationUpdProcDto> updList = new ArrayList<>();
        for (HotelReservationInsDto hotelReservationInsDto : dto) {
            for (DogInfo dogInfo : hotelReservationInsDto.getDogInfo()) {
                HotelReservationUpdProcDto updProcDto = new HotelReservationUpdProcDto();
                updProcDto.setHotelRoomPk(dogInfo.getHotelRoomPk());

                LocalDate fromDate = hotelReservationInsDto.getFromDate();
                LocalDate toDate = hotelReservationInsDto.getToDate();
                // fromDate부터 toDate까지 날짜 배열 생성
                List<LocalDate> dateRange = new ArrayList<>();
                while (!fromDate.isAfter(toDate)) {
                    dateRange.add(fromDate);
                    fromDate = fromDate.plusDays(1);
                }
                /*// 날짜 출력
                for ( LocalDate dateList : dateRange ) {
                    log.info("date : {}", dateList);
                }*/
                updProcDto.setDate(dateRange);
                updList.add(updProcDto);
            }
        }

        /* stream 이용한 코드 3중for문X 2024-01-31*/

//        List<HotelReservationUpdProcDto> procDtoList=new ArrayList<>();
//        dto.stream()
//                .flatMap(hotelReservationInsDto ->
//                    hotelReservationInsDto.getDogInfo().stream()
//                            .map(dogInfo -> {
//                                HotelReservationUpdProcDto procDto=new HotelReservationUpdProcDto();
//                                procDto.setHotelRoomPk(dogInfo.getHotelRoomPk());
//                                List<LocalDate> dateList=hotelReservationInsDto
//                                        .getToDate()
//                                        .datesUntil(hotelReservationInsDto.getToDate().plusDays(1)).collect(Collectors.toList());
//                                procDto.setDate(dateList);
//                                return procDtoList.add(procDto);
//                            })
//                ).collect(Collectors.toList());

        try {
            int affectedRows3 = mapper.updRemainedHotelRoom(updList);
            log.info("affectedRows3 : {}", affectedRows3);
        }catch (Exception e){
            throw new CustomException(ReservationErrorCode.NO_ROOMS_AVAILABLE_FOR_THIS_DATE);
        }
        return new ResVo(Const.SUCCESS);
    }


    //---------------------------------------------------예약 취소--------------------------------------------------------
    @Transactional(rollbackFor = Exception.class)
    public ResVo delHotelReservation(HotelReservationDelDto dto){
        dto.setUserPk(authenticationFacade.getLoginUserPk());
        // 먼저 유저가 예약 했는지 셀렉트
        List<HotelReservationSelProcVo> procVo = mapper.selHotelReservation(dto);
        if(procVo.size() == 0){
            throw new CustomException(ReservationErrorCode.NO_RESERVATION_INFORMATION);  // 예약 정보 없음
        }
        for ( HotelReservationSelProcVo vo: procVo ) {
            dto.setResPk(vo.getResPk()); // 다 같은 pk
            dto.getResDogPkList().add(vo.getResDogPk());
        }
        List<Integer> roomPk = mapper.selHotelRoomPk(dto);
        if (roomPk == null){
            throw new CustomException(ReservationErrorCode.ROOM_PK_SELECT_FAILURE);
        }
        dto.setHotelRoomPk(roomPk);

        // 호텔방강아지테이블 삭제
        int affectedRows1 = mapper.delHotelReservationInfo(dto);
        log.info("affectedRowsHotelReservationInfoDel : {}", affectedRows1);
        if(affectedRows1 == 0){
            throw new CustomException(ReservationErrorCode.FAILED_TO_DELETE_HOTEL_ROOM_DOG_TABLE);
        }
        // 예약 강아지 테이블 삭제
        int affectedRows2 = mapper.delHotelReservationDogInfo(dto);
        log.info("affectedRowsHotelDogInfoDel : {}", affectedRows2);
        if(affectedRows2 == 0){
            throw new CustomException(ReservationErrorCode.FAILED_TO_DELETE_RESERVED_DOG_TABLE);
        }
        // 호텔 예약 테이블 삭제
        int affectedRows3 = mapper.delHotelReservation(dto);
        log.info("affectedRowsHotelReservationDel : {}", affectedRows3);
        if(affectedRows3 == 0){
            throw new CustomException(ReservationErrorCode.FAILED_TO_DELETE_HOTEL_RESERVATION_TABLE);
        }
        // 호텔방 관리테이블 업데이트
        // fromdate, todate를 다 가져와 dateRange로 변경
        List<LocalDate> dateRange = new ArrayList<>();
        for ( HotelReservationSelProcVo vo: procVo ) {
            LocalDate fromDate = vo.getFromDate();
            LocalDate toDate = vo.getToDate();
            while (!fromDate.isAfter(toDate)) {
                dateRange.add(fromDate);
                fromDate = fromDate.plusDays(1);
            } // 여러번 돌아도 같은값
            dto.setDateRange(dateRange);
        }

        HotelReservationUpdProc2Dto pDtoList = new HotelReservationUpdProc2Dto();
        pDtoList.setDate(dateRange);
        pDtoList.setHotelRoomPk(dto.getHotelRoomPk());
        int affectedRows4 = mapper.updRemainedHotelRoom2(pDtoList);
        log.info("affectedRowsHotelRoomUpd : {}", affectedRows4);
        if(affectedRows4 == 0){
            throw new CustomException(ReservationErrorCode.HOTEL_ROOM_MANAGEMENT_TABLE_UPDATE_FAILED);
        }
        return new ResVo(Const.SUCCESS);
    }
    //--------------------------------------------------예약 정보---------------------------------------------------------
    public List<ResInfoVo> getUserReservation(int userPk,int page){
        int perPage=Const.RES_LIST_COUNT_PER_PAGE;
        int pages=(page-1)*Const.RES_LIST_COUNT_PER_PAGE;
        List<ResInfoVo> resInfoVos = mapper.getUserReservation(userPk,perPage,pages);
        List<Integer> resPkList = resInfoVos
                .stream()
                .map(ResInfoVo::getResPk)
                .collect(Collectors.toList());
        List<ResDogInfoVo> resDogInfo = mapper.getDogInfoReservation(resPkList);

        resInfoVos.forEach(resInfoVo -> {
            List<ResDogInfoVo> resDogInfoVoList = resDogInfo
                    .stream()
                    .filter(resDogInfoVo -> resInfoVo.getResPk() == resDogInfoVo.getResPk())
                    .collect(Collectors.toList());
            resInfoVo.setResDogInfoVoList(resDogInfoVoList);
        });
        return resInfoVos;
    }
}

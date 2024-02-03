package com.green.hoteldog.reservation;

import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.CommonErrorCode;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.exceptions.HotelErrorCode;
import com.green.hoteldog.reservation.model.HotelReservationDelDto;
import com.green.hoteldog.reservation.model.HotelReservationInsDto;
import com.green.hoteldog.reservation.model.ResInfoVo;
import com.green.hoteldog.security.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "예약 API",description = "예약 관련 처리")
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService service;
    private final AuthenticationFacade authenticationFacade;
    public void checkUser(){
        if(authenticationFacade.getLoginUserPk()==0){
            throw new CustomException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }
    }
    //---------------------------------------------------호텔 예약--------------------------------------------------------
    @PostMapping("/hotel/res")
    @Operation(summary = "호텔 예약",description = "호텔 예약 처리 기능")
    public ResVo postHotelReservation(@RequestBody List<HotelReservationInsDto> dto){
        return service.postHotelReservation(dto);
    }
    //---------------------------------------------------예약 취소--------------------------------------------------------
    @DeleteMapping("/hotel/res")
    @Operation(summary = "호텔 예약 취소",description = "호텔 예약 취소 관련 처리")
    public ResVo delHotelReservation(HotelReservationDelDto dto){
        return service.delHotelReservation(dto);
    }
    //-------------------------------------------------예약내역 출력-------------------------------------------------------
    @GetMapping
    @Operation(summary = "마이페이지 예약목록 출력",description = "호텔 예약목록출력(Page 당 6개씩 출력)")
    public List<ResInfoVo> getUserReservation(int page){
        if(page<=0){
            throw new CustomException(HotelErrorCode.NON_EXIST_PAGE_DATA);
        }
        checkUser();
        int userPk= authenticationFacade.getLoginUserPk();
        return service.getUserReservation(userPk,page);
    }

}

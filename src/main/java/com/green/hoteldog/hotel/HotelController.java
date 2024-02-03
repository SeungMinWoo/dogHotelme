package com.green.hoteldog.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.green.hoteldog.common.Const;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.CommonErrorCode;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.exceptions.HotelErrorCode;
import com.green.hoteldog.hotel.model.*;
import com.green.hoteldog.security.AuthenticationFacade;
import com.green.hoteldog.user.models.UserHotelFavDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "호텔 API",description = "호텔 관련 처리")
@RequestMapping("/api/hotel")
public class HotelController {
    private final HotelService service;
    private final AuthenticationFacade authenticationFacade;
    public void checkUser(){
        if(authenticationFacade.getLoginUserPk()==0){
            throw new CustomException(CommonErrorCode.UNAUTHORIZED);
        }
    }
    // 0-1 광고+호텔 리스트 api/hotel/{page}
    // 0-2 광고 리스트 api/hotel/ad

    //----------------------------------------------- 호텔 광고 리스트-----------------------------------------------------
    // 새로고침 시 적용
    @GetMapping("/ad")
    @Operation(summary = "호텔 광고 리스트", description = "광고만 바뀌어야 할 때 광고 3개를 랜덤하게 출력하는 기능")
    public List<HotelListSelVo> getHotelAdvertiseList(){
        return service.getHotelAdvertiseList();
    }

    // 광고 + 호텔 리스트
    // 1. 첫 화면 뿌릴 때(유저 별 검색 별 필터링 별로 출력은 서비스에서 구현) 적용
    // 2. 페이지 전환 시 적용

    // 메인 페이지 호텔 리스트 셀렉트 : service단에서 정의
    // 1. 강아지 정보와 주소를 기반으로 첫 기본 화면 셀렉트
    // 1-1. 비회원은 최신순으로 첫 화면 셀렉트
    // 1-2. 회원은 등록한 정보 기반으로 셀렉트
    // 1-2-1. 주소만으로 셀렉트
    // 1-2-1. 주소와 강아지 정보로 셀렉트 (프로젝트 제외)
    // 2. 필터링 처리 후 화면 셀렉트
    // 3. 검색 기반으로 화면 셀렉트 - OKT로 형태소 분석

    // 상세 정렬방식 - 리뷰 많은 순, 별점 높은 순 : XML에서 정의
    //--------------------------------------------------호텔 리스트-------------------------------------------------------
    // 0128 get방식 RequestParam으로 HotelListSelDto객체 받을 때 DogSizeEa를 String에서 int로 컨버트하여 mapping하려 했지만 실패
    @PostMapping
    @Operation(summary = "호텔 + 광고 리스트 출력", description = "<h1>검색, 필터링, 하단 필터링, 사용자화 기능</h1>\n" +
            "<h3><b>mainFilter(상단필터 사용여부)</b>, <b>filterType(하단필터 여부)</b> 값은 항상 필요하며, 이외의 값은 기능에 따라 추가적으로 보내주세요.</h3>\n" +
    "단, 사용자가 입력한 기능 이외의 변수명과 값이 들어 온다면 잘못 입력 에러 리턴")
    public HotelListSelAllVo getHotelList(@RequestParam int page, @RequestBody @Valid HotelListSelDto dto) {
        log.info("HotelListSelDto dto : {}",dto);
        dto.setRowCount(Const.HOTEL_LIST_COUNT_PER_PAGE);
        dto.setPage(page);
        return service.getHotelList(dto);
    }
    //-------------------------------------------------호텔 상세페이지 출력-------------------------------------------------
    @GetMapping
    @Operation(summary = "호텔 상세페이지 전체화면", description = "호텔 상세페이지 전체화면 부분")
    public HotelInfoEntity getHotelDetail(@RequestParam("hotel_pk") int hotelPk){
        HotelMainPageDto dto= new HotelMainPageDto();
        dto.setHotelPk(hotelPk);

        HotelInfoEntity mainPage=service.getHotelDetail(hotelPk);
        return mainPage;
    }
    //------------------------------------------호텔 상세페이지에서 날짜 선택했을때--------------------------------------------
    @GetMapping("/info")
    @Operation(summary = "호텔 상세페이지->날짜 선택 시", description = "상세페이지에서 날짜 선택했을 때 그 날짜별로 가능한 방 리스트")
    public List<HotelRoomEaByDate> whenYouChooseDates(int hotelPk,
                                                      LocalDate startDate,
                                                      LocalDate endDate){

        return service.whenYouChooseDates(hotelPk, startDate, endDate);
    }
    //--------------------------------------호텔 상세페이지에서 날짜 선택, 강아지 선택했을때-------------------------------------
    @GetMapping("/info/dogs")
    @Operation(summary = "호텔 상세페이지->날짜선택->강아지 선택 시", description = "상세페이지에서 날짜 선택하고 강아지 선택 할 시에 나오는 방 리스트<br>" +
            "등록한 강아지들의 사이즈Pk만 입력.")
    public List<HotelRoomEaByDate> whenYouChooseDatesAndDogs(int hotelPk,
                                                             LocalDate startDate,
                                                             LocalDate endDate,
                                                             List<Integer> dogs){
        return service.whenYouChooseDatesAndDogs(hotelPk, startDate, endDate, dogs);
    }

    //-----------------------------------------------------호텔 북마크----------------------------------------------------
    @GetMapping("/mark")
    @Operation(summary = "호텔 북마크(좋아요)", description = "toggle로 처리함<br>. 북마크 등록 시 result : 1 , 북마크 등록 해제 시 : 2")
    public ResVo toggleHotelBookMark(int hotelPk){
        checkUser();
        int userPk=authenticationFacade.getLoginUserPk();
        return service.toggleHotelBookMark(hotelPk,userPk);
    }
    //-----------------------------------------------------호텔 북마크 리스트----------------------------------------------
    @GetMapping("/like")
    @Operation(summary = "좋아요 한 호텔 리스트", description = "좋아요 한 호텔 리스트 Page별로 6개씩 출력")
    public List<HotelBookMarkListVo> getHotelBookmarkList(int page){
        if(page<=0){
            throw new CustomException(HotelErrorCode.NON_EXIST_PAGE_DATA);
        }
        checkUser();
        int userPk=authenticationFacade.getLoginUserPk();
        return service.getHotelBookmarkList(userPk,page);
    }

    //호텔 더미데이터 작성
    @PostMapping("/registration")
    public ResVo hotelRegistration(@RequestPart(required = false) @Schema(hidden = true) List<MultipartFile> pics, @RequestBody HotelInsDto dto){
        log.info("hotelDto : {}",dto);
        return service.hotelRegistration(pics, dto);
    }
    //호텔 사진 수정
    @PutMapping("/pic")
    public ResVo putHotelPic(@RequestPart @Schema(hidden = true)List<MultipartFile> pics,@RequestPart HotelPutPicDto dto){
        dto.setPics(pics);
        return null;
    }
    //호텔 방 등록
    @PostMapping("/room")
    public ResVo hotelRoomRegistration (@RequestPart(required = false) @Schema(hidden = true) MultipartFile roomPic, @RequestBody InsHotelRoomDto dto){
        return service.insHotelRoom(roomPic, dto);
    }



}

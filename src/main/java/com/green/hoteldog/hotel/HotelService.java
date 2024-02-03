package com.green.hoteldog.hotel;


import com.green.hoteldog.common.AppProperties;
import com.green.hoteldog.common.Const;
import com.green.hoteldog.common.MyFileUtils;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.exceptions.AuthorizedErrorCode;
import com.green.hoteldog.exceptions.CommonErrorCode;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.exceptions.HotelErrorCode;
import com.green.hoteldog.hotel.model.*;
import com.green.hoteldog.security.AuthenticationFacade;
import com.green.hoteldog.user.models.UserHotelFavDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import scala.collection.Seq;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelMapper mapper;
    private final AppProperties appProperties;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils myFileUtils;


    //-----------------------------------------------호텔 광고 리스트 셀렉트------------------------------------------------
    public List<HotelListSelVo> getHotelAdvertiseList(){
        return mapper.selHotelAdvertiseList();
    }
    //-----------------------------------------------호텔 리스트 셀렉트----------------------------------------------------
    public HotelListSelAllVo getHotelList(HotelListSelDto dto){
        // 정규표현식
        if(dto.getFromDate() != null){
            boolean dateCheck = Pattern.matches("^[\\d]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", dto.getFromDate());
            if(dateCheck == false){
                throw new CustomException(HotelErrorCode.UNKNOWN_DATE_FORM);
            }
        }
        dto.setUserPk(authenticationFacade.getLoginUserPk());
        // 0. 랜덤 광고 리스트 셀렉
        List<HotelListSelVo> hotelAdvertiseList = mapper.selHotelAdvertiseList();
        log.info("hotelAdvertiseList : {}", hotelAdvertiseList);
        HotelListSelAllVo allVo = new HotelListSelAllVo();
        allVo.setHotelAdvertiseList(hotelAdvertiseList);
        log.info("allVo : {}", allVo);
        // option pk size 값 넣기
        // dog pk size 값 넣기
        if (dto.getHotelOptionPk() != null){
            dto.setOptionPkSize(dto.getHotelOptionPk().size());
        }


        // 1. 등록된 반려견 정보와 주소로 사용자화
        // 1-1. 비회원 첫화면 - 최신순
        if(dto.getUserPk() == 0 && dto.getMainFilter() == 0 && dto.getAddress() == null && dto.getFromDate() == null
                && dto.getToDate() == null && dto.getDogInfo() == null && dto.getSearch() == null
                && dto.getHotelOptionPk() == null){
            allVo.setHotelList(mapper.selHotelListToNonMember(dto));
            log.info("allVo : {}", allVo);
            return allVo;
            // 1-2. 회원 첫화면 - 주소, 반려견 강아지 사이즈(개별방 단체방 모두 고려)
        } else if (dto.getMainFilter() == 0 && dto.getUserPk() > 0 && dto.getAddress() == null && dto.getFromDate() == null
                && dto.getToDate() == null && dto.getDogInfo() == null && dto.getSearch() == null
                && dto.getHotelOptionPk() == null){

            // 1-2-1. 회원 pk로 강아지 사이즈, 주소 pk 셀렉
            List<HotelListSelProcDto> pDto = mapper.selUserInfoToUserPk(dto);
            for ( HotelListSelProcDto procDto : pDto ) {
                dto.setAddress(procDto.getAddress()); // 다 같은값
                if(procDto.getDogSizePks() > 0){
                    dto.getDogSizePks().add(procDto.getDogSizePks());
                }
            }
            if(dto.getDogSizePks().size() != 0){
                dto.setDogPkSize(dto.getDogSizePks().size());
            }

            // 1-2-1-1. 등록된 강아지가 없을 때
            if(dto.getDogSizePks().size() == 0){
                allVo.setHotelList(mapper.selHotelListAsUserAddress(dto));
                log.info("allVo : {}", allVo);
                return allVo;
            }else {
                // 1-2-1-2. 등록 된 강아지가 있을 때
                allVo.setHotelList(mapper.selHotelListAsUserAddressAndDogInformation(dto));
                log.info("allVo : {}", allVo);
                return allVo;
            }
            // 2. 호텔 이름 검색 시 리스트
        } else if (dto.getMainFilter() == 0 && dto.getSearch() != null && dto.getAddress() == null && dto.getFromDate() == null
                && dto.getToDate() == null && dto.getDogInfo() == null && dto.getHotelOptionPk() == null){
            // 2-1. 정확하게 입력된 호텔 이름을 먼저 셀렉트
            List<HotelListSelVo> accurateHotelList = mapper.selHotelListToAccurateSearch(dto);
            log.info("accurateHotelList.size() : {}", accurateHotelList.size());
            if(accurateHotelList.size() > 0){
                allVo.setHotelList(accurateHotelList);
                return allVo;
            }

            // 2-2. 형태소 분석기 사용, String을 각각의 단어로 String 리스트를 만들어 줍니다.
            // step 1 : 문장 분리
            // step 2 : 형태소 분석 및 품사 태깅

            dto.setSearch(dto.getSearch().replaceAll("애견", ""));
            dto.setSearch(dto.getSearch().replaceAll("호텔", ""));
            log.info("search : {}", dto.getSearch());

            String text = dto.getSearch();
            CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);
            Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
            List<String> tokensToStrList = OpenKoreanTextProcessorJava.tokensToJavaStringList(tokens);
            dto.setTokensToStrList(tokensToStrList);
            log.info("tokensToStrList : {}", tokensToStrList);

            List<HotelListSelVo> hotelList = mapper.selHotelListToSearch(dto);
            log.info("hotelList = {}", hotelList);

            // page가 1일 때에만 정확한 검색어를 첫번째로 저장해서 보냅니다.
            // 정확한 검색어 수만큼 따라 마지막 인덱스 제거
            accurateHotelList.addAll(hotelList);
            allVo.setHotelList(accurateHotelList);
            // 나중에 시간날 때 배열과 일치하는 순으로 정확도로 정렬하는 코드 작성
            // String[] filteredArray = filterAndSortStrings(vo.getHotelNm(), "a", "e");
            /*
            예시
            "부평싸이호텔" 검색 시
	        -> 부평싸이호텔 1개 셀렉트
	        -> 유사한 검색어도 셀렉
            "부평싸일호텔" 검색 시
	        -> 유사한 검색어 셀렉
            */

            // 3. 필터링 시 리스트
        } else if (dto.getMainFilter() == 1){
            List<LocalDate> dateRange = new ArrayList<>();
            if(dto.getFromDate() != null && dto.getToDate() != null){
                //  LocalDate, Calendar 방법 두 가지

                // "yyyy-MM-dd" 포맷을 사용하여 문자열을 LocalDate로 파싱
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fromDate = LocalDate.parse(dto.getFromDate(), formatter);
                LocalDate toDate = LocalDate.parse(dto.getToDate(), formatter);

                // 년, 월, 일 추출
                int fromYear = fromDate.getYear();
                int fromMonth = fromDate.getMonthValue();
                int fromDay = fromDate.getDayOfMonth();

                int toYear = toDate.getYear();
                int toMonth = toDate.getMonthValue();
                int toDay = toDate.getDayOfMonth();

                LocalDate fromDateUnit = LocalDate.of(fromYear, fromMonth, fromDay);
                LocalDate toDateUnit = LocalDate.of(toYear, toMonth, toDay);

                // fromDate부터 toDate까지 날짜 배열 생성
                while (!fromDateUnit.isAfter(toDateUnit)) {
                    dateRange.add(fromDateUnit);
                    fromDateUnit = fromDateUnit.plusDays(1);
                }
                // 날짜 출력
                for ( LocalDate dateList : dateRange ) {
                    log.info("date : {}", dateList);
                }
                dto.setDate(dateRange);

                // 날짜 하나만 들어 올 때 (대실)
            } else if(dto.getFromDate() != null && dto.getFromDate() == null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fromDate = LocalDate.parse(dto.getFromDate(), formatter);
                LocalDate date = LocalDate.of(fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth());
                dto.getDate().add(date);
            }

            List<String> strDateList = new ArrayList<>();
            for ( LocalDate dateList : dateRange ) {
                strDateList.add(dateList.toString());
            }

            // 개별 방에 관한 호텔 pk 셀렉
            // 날짜, 강아지 정보가 안들어 오면 실행x
            if(dto.getFromDate() != null && dto.getDogInfo() != null){
                List<Integer> list = new ArrayList<>();
                DogSizeInfoIn dogSizeInfo = new DogSizeInfoIn();
                dogSizeInfo.setDates(strDateList);
                for ( DogSizeEa ea : dto.getDogInfo() ) {
                    dogSizeInfo.setDogSize(ea.getDogSize());
                    dogSizeInfo.setDogCount(ea.getDogCount());
                    List<Integer> hotelPk1 = mapper.selHotelPkToIndividualDogInfo(dogSizeInfo);
                    list.addAll(hotelPk1);
                }
                // 단체 방에 관한 호텔 pk 셀렉
                DogSizeInfoGr dogSizeinfoGr = new DogSizeInfoGr();
                dogSizeinfoGr.setDate(dateRange);
                List<Integer> dogSize = new ArrayList<>();
                int allCount = 0;
                for ( DogSizeEa ea : dto.getDogInfo() ) {
                    allCount =+ ea.getDogCount();
                    dogSize.add(ea.getDogSize());
                }
                dogSizeinfoGr.setAllDogCount(allCount);
                Integer maxValue = dogSize.stream()
                        .mapToInt(x -> x)
                        .max()
                        .orElse(0); // 예외 시
                log.info("maxValue : {}", maxValue);
                dogSizeinfoGr.setBiggestDogSize(maxValue);
                List<Integer> hotelPk2 = mapper.selHotelPkToGroupDogInfo(dogSizeinfoGr);
                list.addAll(hotelPk2);
                // 단체 방 + 개별 방 종합 호텔 pk 셀렉 구현


                // 중복 제거
                List<Integer> filteredPk = list.stream().distinct().collect(Collectors.toList());
                log.info("filteredPk : {}", filteredPk);
                dto.setFilteredPk(filteredPk);
            }
            List<HotelListSelVo> voList = mapper.selHotelListToFilter(dto);
            allVo.setHotelList(voList);

            log.info("allVo : {}", allVo);
            return allVo;
        }
        return allVo;
    }
    // 영웅



    //----------------------------------------------호텔 북마크-----------------------------------------------------------
    public ResVo toggleHotelBookMark(int hotelPk,int userPk){
        int result=mapper.delHotelBookMark(userPk,hotelPk);
        if(result==1){
            return new ResVo(2);
        }
        int result2= mapper.insHotelBookMark(userPk,hotelPk);
        return new ResVo(result2);
    }
    //----------------------------------------------북마크 한 호텔 리스트---------------------------------------------------
    public List<HotelBookMarkListVo> getHotelBookmarkList(int userPk,int page){
        int perPage=Const.HOTEL_LIST_COUNT_PER_PAGE;
        int pages=page*Const.HOTEL_FAV_COUNT_PER_PAGE;
        List<HotelBookMarkListVo> getBookMarkList=mapper.getHotelBookMark(userPk,pages,perPage);
        List<Integer> pkList=getBookMarkList
                .stream()
                .map(HotelBookMarkListVo::getHotelPk)
                .collect(Collectors.toList());

        List<HotelBookMarkPicVo> picVoList=mapper.getHotelBookMarkPic(pkList);

        getBookMarkList.forEach(vo ->
                picVoList.stream()
                        .filter(picVo -> vo.getHotelPk() == picVo.getHotelPk())
                        .findFirst()
                        .ifPresent(picVo -> vo.setHotelPic(picVo.getPic()))
        );
        return getBookMarkList;

    }
    //--------------------------------------------호텔 상세페이지----------------------------------------------------------
    public HotelInfoEntity getHotelDetail(int hotelPk){
        if(hotelPk>0) {
            int userPk= authenticationFacade.getLoginUserPk();
            log.info("userPk : {}",userPk);

            //메인페이지 객체 생성
            HotelInfoEntity hotelInfoEntity = new HotelInfoEntity();
            // 호텔 기본적인 정보.
            HotelInfoVo hotelInfoVo = mapper.getHotelDetail(hotelPk);
            //호텔 사진 넣어줌.
            hotelInfoVo.setPics(mapper.getHotelPics(hotelPk));
            List<String> option = mapper.hotelOptionInfo(hotelPk);
            hotelInfoVo.setHotelOption(option);

            //좋아요 많은 갯수대로 호텔에 적힌 리뷰 최대 3개까지 가져옴.
            List<HotelReviewVo> reviewThree = mapper.getHotelReviewThree(hotelPk);
            int countReview = mapper.isMoreHotelReview(hotelPk);
            if (countReview > 3) {
                hotelInfoVo.setIsMoreReview(1);//리뷰 더있니 => 0 to 1
                log.info("isMoreReview : {}", hotelInfoVo.getIsMoreReview());
            }
            if(reviewThree.size()>0){
                hotelInfoVo.setReviewList(reviewThree);
            }
            List<HotelRoomInfoVo> roomInfoVos=mapper.getHotelRoomInfo(hotelPk);
            hotelInfoVo.setRoomList(roomInfoVos);


            hotelInfoEntity.setHotelInfoVo(hotelInfoVo);

            List<LocalDate> twoMonthDate = getTwoMonth();
            //twoMonthDate : 두달동안 날짜 리스트(LocalDate 타입)

            //twoMonth : String 타입 리스트.
            List<String> twoMonth = twoMonthDate
                    .stream()
                    .map(localDate -> localDate.toString())
                    .collect(Collectors.toList());

            HotelRoomAbleListDto ableListDto = new HotelRoomAbleListDto();
            ableListDto.setHotelPk(hotelPk);
            //메인페이지 첫화면은 호텔pk만 보내고 정리해서 줌.
            List<HotelRoomResInfoByMonth> hotelResInfoVos = iWillShowYouAbleDatesWithRoom(ableListDto);
            //박스갈이 & 데이터빼내기&검증
            List<HotelRoomEaByDate> eaByDates = new ArrayList<>();

            for (String date : twoMonth) {
                HotelRoomEaByDate eaByDate = new HotelRoomEaByDate();
                eaByDate.setDate(date);
                eaByDates.add(eaByDate);
                log.info("date : {}", date);
            }
            //들어간 데이터 중 날짜정보만 빼옴.
            List<String> checkDate = eaByDates
                    .stream()
                    .map(HotelRoomEaByDate::getDate)
                    .toList();
            if (!checkDate.containsAll(twoMonth)) {
                throw new CustomException(CommonErrorCode.CONFLICT);
            }
            //근데 이 위에것 굳이 할 필요가 있을까??
            //람다식?
            //forEach반복문 .
            eaByDates.forEach(date -> {     //eaByDates 의 String 하나를 date 으로 지정.
                List<HotelRoomEa> roomEas = hotelResInfoVos     //RoomEaByDate 안의 객체인 List<HotelRoomEa> 생성 후 스트림.
                        .stream()
                        .filter(getDate -> date.getDate().equals(getDate.getRoomDate()))//날짜와 방의 날짜가 같은것 필터.
                        .map(roomInfo -> {//맵 생성
                            HotelRoomEa hotelRoomEa = new HotelRoomEa();
                            hotelRoomEa.setHotelRoomNm(roomInfo.getHotelRoomNm());
                            hotelRoomEa.setRoomLeftEa(roomInfo.getRoomLeftEa()); //넣어줌
                            return hotelRoomEa;//반환
                        })
                        .collect(Collectors.toList());//리스트로 반환

                date.setRoomEas(roomEas); //하나하나 넣어줌.
                log.info("roomEas : {}", roomEas);
            });

            hotelInfoEntity.setRoomEaByDates(eaByDates);

            if (userPk >= 1) {
                List<MyDog> myDogList = mapper.getMyDogs(userPk);
                if(myDogList.size()>0){
                    hotelInfoEntity.setMyDogList(myDogList);
                }
                log.info("myDogList : {}", hotelInfoEntity.getMyDogList());
            }
            return hotelInfoEntity;
        }
        throw new CustomException(CommonErrorCode.RESOURCE_NOT_FOUND);
    }


    //----------------------------------------날짜 선택했을때 가능한 방 리스트------------------------------------------------
    public List<HotelRoomEaByDate> whenYouChooseDates(int hotelPk,LocalDate startDate,LocalDate endDate){
        if (hotelPk == 0 || startDate == null || endDate == null) {
            throw new CustomException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }
        if(hotelPk>0) {
            HotelRoomAbleListDto ableListDto = new HotelRoomAbleListDto();
            ableListDto.setHotelPk(hotelPk);
            ableListDto.setEndDate(endDate.toString());
            ableListDto.setStartDate(startDate.toString());
            List<HotelRoomResInfoByMonth> areYouChooseDates = iWillShowYouAbleDatesWithRoom(ableListDto);

            List<HotelRoomEaByDate> whenYouSelDates = new ArrayList<>();
            List<LocalDate> getList = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
            List<String> getDateList = getList
                    .stream()
                    .map(date -> date.toString())
                    .collect(Collectors.toList());

            getList.forEach(System.out::println);
            List<HotelRoomEaByDate> updatedList = new ArrayList<>();

            getDateList.forEach(date -> {
                List<HotelRoomEa> eaList = areYouChooseDates
                        .stream()
                        .filter(s -> s.getRoomDate().equals(date.toString()))
                        .map(roomInfo -> {
                            HotelRoomEa roomEa = new HotelRoomEa();
                            roomEa.setHotelRoomNm(roomInfo.getHotelRoomNm());
                            roomEa.setRoomLeftEa(roomInfo.getRoomLeftEa());
                            return roomEa;
                        })
                        .collect(Collectors.toList());

                HotelRoomEaByDate eaByDate = new HotelRoomEaByDate();
                eaByDate.setDate(date.toString());
                eaByDate.setRoomEas(eaList);
                log.info("eaList : {}", eaList);
                updatedList.add(eaByDate);
            });

            whenYouSelDates.addAll(updatedList);
            return whenYouSelDates;
        }
        throw new CustomException(CommonErrorCode.RESOURCE_NOT_FOUND);
    }
    //------------------------------------날짜랑 강아지 선택했을 때 가능한 방 리스트--------------------------------------------
    public List<HotelRoomEaByDate> whenYouChooseDatesAndDogs(int hotelPk,LocalDate startDate,LocalDate endDate,List<Integer> dogs){
        if(hotelPk==0||startDate==null||endDate==null||dogs.size()==0){
            throw new CustomException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }
        if(dogs.size()>0) {
            int howMany = dogs.size();
            int large = dogs
                    .stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0);
            log.info("hotelPk : {}", hotelPk);
            log.info("startDate : {}", startDate);
            log.info("endDate : {}", endDate);
            log.info("dogs : {}", dogs);

            List<LocalDate> getList=startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
            List<String> getListDates=getList
                    .stream()
                    .map(date -> date.toString())
                    .collect(Collectors.toList());

            getList.forEach(System.out::println);

            List<HotelRoomEaByDate> whenYouChooseDatesAndDogs=new ArrayList<>();
            List<HotelRoomResInfoByMonth> areYouSure =mapper.getHotelFilterRoomResInfo(hotelPk,startDate.toString(),endDate.toString(),howMany,large);
            
            getListDates.forEach(date -> {
                List<HotelRoomEa> eaList = areYouSure
                        .stream()
                        .filter(s -> s.getRoomDate().equals(date.toString()))
                        .map(roomInfo -> {
                            HotelRoomEa roomEa = new HotelRoomEa();
                            roomEa.setHotelRoomNm(roomInfo.getHotelRoomNm());
                            roomEa.setRoomLeftEa(roomInfo.getRoomLeftEa());
                            return roomEa;
                        })
                        .collect(Collectors.toList());

                HotelRoomEaByDate eaByDate = new HotelRoomEaByDate();
                eaByDate.setDate(date.toString());
                eaByDate.setRoomEas(eaList);

                whenYouChooseDatesAndDogs.add(eaByDate);
                log.info("eaByDates : {}", eaByDate);
            });
            return whenYouChooseDatesAndDogs;
        }
        throw new CustomException(CommonErrorCode.RESOURCE_NOT_FOUND);
    }
    //------------------------------------------가능한 방 리스트 필터링-----------------------------------------------------

    public List<HotelRoomResInfoByMonth> iWillShowYouAbleDatesWithRoom(HotelRoomAbleListDto dto) {
        if (dto.getHotelPk() == 0) {
            throw new CustomException(HotelErrorCode.NON_EXIST_HOTEL_PK);
        }
        //시작 끝 날짜만 입력 되어있는 상태.(예약 2단계)
        if (!(dto.getEndDate() == null && dto.getStartDate() == null)) {
            log.info("hotelPk : {}", dto.getHotelPk());
            log.info("startDate : {}", dto.getStartDate());
            log.info("endDate : {}", dto.getEndDate());
            List<HotelRoomResInfoByMonth> resInfoByMonths=mapper.getHotelRoomResInfo(dto.getHotelPk(), dto.getStartDate(), dto.getEndDate());
            if(resInfoByMonths.size()==0){
                throw new CustomException(HotelErrorCode.NON_EXIST_ROOM_DATE);
            }
            return resInfoByMonths;
            // 시작 , 끝 날짜 설정X, (예약 1단계 or 상세페이지)
        } else {
            List<LocalDate> twoMonthDate = getTwoMonth();
            String startDate = twoMonthDate.get(0).toString();//시작날짜 : 요번달 첫날
            String endDate = twoMonthDate.get(twoMonthDate.size() -1).toString();//끝나는날짜 : 다음달 말일
            log.info("hotelPk : {}", dto.getHotelPk());

            List<HotelRoomResInfoByMonth> infoByMonths=mapper.getHotelRoomResInfo(dto.getHotelPk(), startDate, endDate);
            if(infoByMonths.size()==0){
                throw new CustomException(HotelErrorCode.NON_EXIST_ROOM_DATE);
            }
            return infoByMonths;
        }
    }
    //---------------------------------------------2달 생성--------------------------------------------------------------
    public List<LocalDate> getTwoMonth(){
        LocalDate today=LocalDate.now();
        List<LocalDate> twoMonthDate=new ArrayList<>();
        //요번달 날짜,
        for (int i = 1; i < today.lengthOfMonth() ; i++) {
            LocalDate localDate=LocalDate.now().plusDays(i- today.getDayOfMonth());
            twoMonthDate.add(localDate);
        }
        //다음달 날짜,
        for (int i = 0; i <= today.plusMonths(1).lengthOfMonth(); i++) {
            LocalDate localDate=LocalDate.now().plusMonths(1).plusDays(i- today.getDayOfMonth());
            twoMonthDate.add(localDate);
        }
        return twoMonthDate;
    }
    //혹시나 몰라서....
    public List<LocalDate> getMonthIChoose(int year, int month){

        List<LocalDate> monthDateList=new ArrayList<>();

        LocalDate today=LocalDate.of(year,month,1);
        for (int i = 1; i < today.lengthOfMonth() ; i++) {
            LocalDate localDate=LocalDate.now().plusDays(i- today.getDayOfMonth());
            monthDateList.add(localDate);
        }
        return monthDateList;
    }

    // 호텔 더미데이터 작성
    @Transactional(rollbackFor = Exception.class)
    public ResVo hotelRegistration(List<MultipartFile> pics, HotelInsDto dto){
        dto.setUserPk(authenticationFacade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            //예외처리
            return new ResVo(0);
        }
        mapper.insHotel(dto);
        if(pics != null){
            List<String> hotelPics = new ArrayList<>();
            String target = "/hotel/"+dto.getHotelPk();
            HotelInsPicDto picDto = new HotelInsPicDto();
            for(MultipartFile file : pics){
                String saveFileNm = myFileUtils.transferTo(file,target);
                hotelPics.add(saveFileNm);
            }
            picDto.setPics(hotelPics);
            picDto.setHotelPk(dto.getHotelPk());
            picDto.setUserPk(dto.getUserPk());
            try {
                mapper.insHotelPics(picDto);
            }catch (Exception e){
                myFileUtils.delFolderTrigger(target);
                throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
            }
        }
        dto.getAddressDto().setHotelPk(dto.getHotelPk());
        mapper.insHotelOption(dto);
        mapper.insHotelWhere(dto.getAddressDto());
        return new ResVo(1);
    }
    //호텔 사진 수정
    public ResVo putHotelPics(HotelPutPicDto dto){
        dto.setUserPk(authenticationFacade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        //mapper.delHotelPic()
        List<String> hotelPics = new ArrayList<>();
        String target = "/hotel/" + dto.getHotelPk();
        for(MultipartFile file : dto.getPics()){
            String saveFileNm = myFileUtils.transferTo(file,target);
            hotelPics.add(saveFileNm);
        }
        HotelInsPicDto picDto = new HotelInsPicDto();
        picDto.setUserPk(dto.getUserPk());
        picDto.setHotelPk(dto.getHotelPk());
        picDto.setPics(hotelPics);
        try {
            mapper.insHotelPics(picDto);
        }catch (Exception e){
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResVo insHotelRoom(MultipartFile hotelPic ,InsHotelRoomDto dto){
        dto.setUserPk(authenticationFacade.getLoginUserPk());
        if(dto.getUserPk() == 0){
            return new ResVo(0);
        }
        if(hotelPic != null){
            String target = "/hotel/"+dto.getHotelPk() + "/room/" + dto.getHotelRoomNm();
            String saveFileNm = myFileUtils.transferTo(hotelPic,target);
            dto.setRoomPic(saveFileNm);
        }
        mapper.insHotelRoomInfo(dto);
        List<LocalDate> towMonth = getTwoMonth();
        InsHotelRoomDateInfoDto dateInfoDto = new InsHotelRoomDateInfoDto();
        dateInfoDto.setHotelRoomPk(dto.getHotelRoomPk());
        dateInfoDto.setHotelLeftEa(dto.getHotelRoomEa());
        dateInfoDto.setRoomDate(towMonth);
        mapper.insHotelRoomInfoDate(dateInfoDto);
        return new ResVo(1);

    }
}

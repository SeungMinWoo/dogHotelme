package com.green.hoteldog.hotel;

import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.hotel.model.*;
import com.green.hoteldog.security.AuthenticationFacade;
import com.green.hoteldog.user.models.UserHotelFavDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
public class HotelServiceTest {
    @InjectMocks
    private HotelService hotelService;
    @Mock
    private HotelMapper hotelMapper;
    @Mock
    private AuthenticationFacade authenticationFacade;


    @Test
    public void testHotelBookMark(){
        UserHotelFavDto dto=new UserHotelFavDto();
        dto.setHotelPk(1);
        dto.setUserPk(1);

        when(authenticationFacade.getLoginUserPk()).thenReturn(1);
        when(hotelMapper.delHotelBookMark(1,2)).thenReturn(1);
        ResVo result=hotelService.toggleHotelBookMark(1,2);

        assertThat(new ResVo(1),is(equalTo(result)));
        ResVo result2=hotelService.toggleHotelBookMark(1,2);
        assertThat(new ResVo(1),is(equalTo(result2)));
    }

    @Test
    public void hotelDetailPage() {
        HotelMainPageDto dto = new HotelMainPageDto();
        dto.setHotelPk(1);
        dto.setUserPk(1);

        when(authenticationFacade.getLoginUserPk()).thenReturn(1);
        HotelInfoVo hotelInfoVo = new HotelInfoVo();
        when(hotelMapper.getHotelDetail(dto.getHotelPk())).thenReturn(hotelInfoVo);
        when(hotelMapper.getHotelPics(dto.getHotelPk())).thenReturn(Arrays.asList("pic1", "pic2"));
        when(hotelMapper.hotelOptionInfo(dto.getHotelPk())).thenReturn(Arrays.asList("option1", "option2"));

        HotelInfoEntity result = hotelService.getHotelDetail(dto.getHotelPk());

        assertThat(hotelInfoVo, is(equalTo(result.getHotelInfoVo())));
        assertThat(Arrays.asList("pic1", "pic2"),
                is(equalTo(result.getHotelInfoVo().getPics())));
        assertThat(Arrays.asList("option1", "option2"),
                is(equalTo(result.getHotelInfoVo().getHotelOption())));
        when(hotelMapper.getHotelReviewThree(dto.getHotelPk())).thenReturn(Arrays.asList(new HotelReviewVo(), new HotelReviewVo(), new HotelReviewVo()));
        when(hotelMapper.isMoreHotelReview(dto.getHotelPk())).thenReturn(4);
        when(hotelMapper.getMyDogs(dto.getUserPk())).thenReturn(Arrays.asList(new MyDog(), new MyDog(), new MyDog()));

        HotelInfoEntity iThink = hotelService.getHotelDetail(dto.getHotelPk());
        assertThat(3, is(equalTo(result.getHotelInfoVo().getReviewList().size())));
        assertThat(1, is(equalTo(result.getHotelInfoVo().getIsMoreReview())));
        assertThat(3, is(equalTo(result.getMyDogList().size())));

        HotelRoomAbleListDto ableListDto = new HotelRoomAbleListDto();
        ableListDto.setHotelPk(1);

        List<LocalDate> twoMonthDate = Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        when(hotelService.getTwoMonth()).thenReturn(twoMonthDate);

        List<HotelRoomResInfoByMonth> iExpect =
                hotelService.iWillShowYouAbleDatesWithRoom(ableListDto);

        assertThat(twoMonthDate.size(), is(equalTo(iExpect)));

        for (int i = 0; i < twoMonthDate.size(); i++) {
            assertThat(twoMonthDate.get(i).toString(), is(equalTo(iExpect.get(i).toString())));
        }
//        List<HotelRoomEaByDate> eaByDates=hotelService.getHotelDetail(dto);
//
//        assertThat(twoMonthDate.size(), is(equalTo(eaByDates.size())));
//
//       
//        for (int i = 0; i < twoMonthDate.size(); i++) {
//            assertThat(twoMonthDate.get(i).toString(), is(equalTo(eaByDates.get(i).getDate())));
//
//
//            LocalDate toDayDate = twoMonthDate.get(i);
//            List<HotelRoomEa> roomEas = eaByDates.get(i).getRoomEas();
//            List<HotelRoomResInfoByMonth> filteredRooms = eaByDates.stream()
//                    .filter(roomInfo -> roomInfo.getDate())
//                    .collect(Collectors.toList());
//
//            assertThat(filteredRooms.size(),is(equalTo(roomEas.size())));
//
//            for (int j = 0; j < filteredRooms.size(); j++) {
//                assertThat(filteredRooms.get(j).getRoomLeftEa()
//                        ,is(equalTo(roomEas.get(j).getRoomLeftEa())));
//            }
        }
    }


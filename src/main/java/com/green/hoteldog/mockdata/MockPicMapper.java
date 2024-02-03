package com.green.hoteldog.mockdata;

import com.green.hoteldog.mockdata.models.MockPicDto;
import com.green.hoteldog.mockdata.models.MockPicsDto;
import com.green.hoteldog.mockdata.models.MockTestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MockPicMapper {

    List<Integer> selReviewPk(MockTestDto dto);
    List<Integer> selHotelPk(MockTestDto dto);
    List<Integer> selRoomPk(MockTestDto dto);
    List<Integer> selDogPk(MockTestDto dto);
    int insMockReviewPic(MockPicsDto dto);
    int insMockHotelPic(MockPicsDto dto);
    int insMockHotelRoomPic(MockPicDto dto);
    int insMockDogPic(MockPicDto dto);

}

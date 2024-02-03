package com.green.hoteldog.dog;

import com.green.hoteldog.dog.models.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DogMapper {
    int insUserDog (InsUserDogDto dto);
    int updUserDog (UpdUserDogDto dto);
    int delUserDog (DelUserDogDto dto);
    List<GetDogListVo> selUserDog (GetUserDogDto dto);
    int setUserDogPic(SetUserDogPicDto dto);
}

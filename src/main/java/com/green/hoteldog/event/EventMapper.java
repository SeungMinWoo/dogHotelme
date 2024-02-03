package com.green.hoteldog.event;

import com.green.hoteldog.event.model.EventInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {
    List<EventInfoVo> getEvents();

}

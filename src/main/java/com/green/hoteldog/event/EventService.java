package com.green.hoteldog.event;
import com.green.hoteldog.event.model.EventInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventMapper eventMapper;
    public List<EventInfoVo> getEvent(){
        List<EventInfoVo> getHotelEvents=eventMapper.getEvents();
        int eventsListSize=getHotelEvents.size();
        return getHotelEvents;
    }
}

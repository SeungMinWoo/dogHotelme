package com.green.hoteldog.event;

import com.green.hoteldog.event.model.EventInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
@Tag(name = "이벤트 API",description = "이벤트 관련 처리")
public class EventController {
    private final EventService eventService;
    @GetMapping()
    @Schema(name = "이벤트")
    public List<EventInfoVo> getEvent(){
        List<EventInfoVo> getHotelEvents;
        getHotelEvents=eventService.getEvent();
        return getHotelEvents;
    }
}

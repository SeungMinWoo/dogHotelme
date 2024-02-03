package com.green.hoteldog.event.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "이벤트")
public class EventInfoVo {
    @Schema(name = "이벤트 사진")
    private String pic;
    @Schema(name = "이벤트 URL")
    private String url;
}


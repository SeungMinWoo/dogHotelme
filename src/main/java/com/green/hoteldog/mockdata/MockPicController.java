package com.green.hoteldog.mockdata;

import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.mockdata.models.MockTestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class MockPicController {
    private final MockPicService service;


    @PostMapping
    public ResVo testMockPics(@RequestPart List<MultipartFile> pics
            ,@RequestPart MockTestDto dto){
        dto.setPics(pics);
        dto.setStartIdx((dto.getPage()-1)*dto.getRowCount());
        return service.insMockPic(dto);
    }
}

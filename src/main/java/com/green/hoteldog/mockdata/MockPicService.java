package com.green.hoteldog.mockdata;

import com.green.hoteldog.common.MyFileUtils;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.mockdata.models.MockPicDto;
import com.green.hoteldog.mockdata.models.MockPicsDto;
import com.green.hoteldog.mockdata.models.MockTestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockPicService {
    private final MockPicMapper mapper;
    private final MyFileUtils fileUtils;

    public ResVo insMockPic(MockTestDto dto){
        log.info("MockTestDto : {}",dto);
        if (dto.getType() == 1){
            return insReviewMockPic(dto);
        }
        if (dto.getType() == 2){
            return insHotelMockPic(dto);
        }
        if (dto.getType() == 3){
            return insRoomMockPic(dto);
        }
        if (dto.getType() == 4){
            return insDogMockPic(dto);
        }
        return null;
    }


    public ResVo insReviewMockPic(MockTestDto dto) {
        List<Integer> pkList = mapper.selReviewPk(dto);
        if(pkList == null){
            throw new RuntimeException();
        }
        int x = 0;
        for (Integer pk : pkList) {
            MockPicsDto mDto = new MockPicsDto();
            List<String> picsList = new ArrayList<>();
            mDto.setPk(pk);
            log.info("pics.size : {}", dto.getPics());
            int picsSize = dto.getPics().size();
            log.info("picsSize : {}",picsSize);
            for (int i = 0; i < 3; i++) {
                String target = "/review/" + pk;
                String saveFileNm = fileUtils.transferTo(dto.getPics().get(x+i), target);
                log.info("saveFileNm : {}", saveFileNm);
                picsList.add(saveFileNm);
            }
            log.info("picsList : {}", picsList);
            mDto.setPics(picsList);
            log.info("MockPicsDto dto : {}", dto);

            mapper.insMockReviewPic(mDto);
            x += 3;
        }
        return new ResVo(1);

    }

    public ResVo insHotelMockPic(MockTestDto dto) {
        List<Integer> pkList = mapper.selHotelPk(dto);
        if(pkList == null){
            throw new RuntimeException();
        }
        int x = 0;
        for (Integer pk : pkList) {
            MockPicsDto mDto = new MockPicsDto();
            List<String> picsList = new ArrayList<>();
            mDto.setPk(pk);
            log.info("pics.size : {}", dto.getPics());
            int picsSize = dto.getPics().size();
            log.info("picsSize : {}",picsSize);
            for (int i = 0; i < 5; i++) {
                String target = "/hotel/" + pk;
                String saveFileNm = fileUtils.transferTo(dto.getPics().get(x+i), target);
                log.info("saveFileNm : {}", saveFileNm);
                picsList.add(saveFileNm);
            }
            log.info("picsList : {}", picsList);
            mDto.setPics(picsList);
            log.info("MockPicsDto dto : {}", dto);

            mapper.insMockHotelPic(mDto);
            x += 5;
        }
        return new ResVo(1);
    }
    public ResVo insRoomMockPic(MockTestDto dto) {
        List<Integer> pkList = mapper.selRoomPk(dto);
        if(pkList == null){
            throw new RuntimeException();
        }
        int x = 0;
        for (Integer pk : pkList) {
            MockPicDto mDto = new MockPicDto();
            mDto.setPk(pk);
            log.info("pics.size : {}", dto.getPics());
            int picsSize = dto.getPics().size();
            log.info("picsSize : {}",picsSize);
            String target = "/room/" + pk;
            String saveFileNm = fileUtils.transferTo(dto.getPics().get(x), target);
            log.info("saveFileNm : {}", saveFileNm);
            mDto.setPic(saveFileNm);
            log.info("MockPicsDto dto : {}", dto);
            mapper.insMockHotelRoomPic(mDto);
            x++;
        }
        return new ResVo(1);
    }
    public ResVo insDogMockPic(MockTestDto dto) {
        log.info("MockTestDtoDog : {}",dto);
        List<Integer> pkList = mapper.selDogPk(dto);
        if(pkList == null){
            throw new RuntimeException();
        }
        int x = 0;
        for (Integer pk : pkList) {
            MockPicDto mDto = new MockPicDto();
            mDto.setPk(pk);
            log.info("pics.size : {}", dto.getPics());
            int picsSize = dto.getPics().size();
            log.info("picsSize : {}",picsSize);
            String target = "/room/" + pk;
            String saveFileNm = fileUtils.transferTo(dto.getPics().get(x), target);
            log.info("saveFileNm : {}", saveFileNm);
            mDto.setPic(saveFileNm);
            log.info("MockPicsDto dto : {}", dto);
            mapper.insMockDogPic(mDto);
            x++;
        }
        return new ResVo(1);
    }
}

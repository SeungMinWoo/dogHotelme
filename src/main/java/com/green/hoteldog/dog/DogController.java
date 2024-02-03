package com.green.hoteldog.dog;

import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.dog.models.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dog")
public class DogController {
    private final DogService service;

    //유저가 등록한 강아지 리스트 호출
    @GetMapping
    @Operation(summary = "유저 강아지 리스트"
            ,description = "유저가 등록한 강아지 리스트")
    public List<GetDogListVo> selUserDogList(GetUserDogDto dto){
        return service.selUserDogList(dto);
    }
    //유저 강아지 등록
    @PostMapping
    @Operation(summary = "유저 강아지 등록",description = "포스트맨을 통해 테스트")
    public ResVo postUserDog(@RequestPart MultipartFile pic,
                             @RequestPart @Valid InsUserDogDto dto){
        dto.setDogPic(pic);
        return service.insUserDog(dto);
    }
    //유저 강아지 정보 수정
    @PutMapping
    @Operation(summary = "유저 강아지 정보 수정",description = "강아지 사진 외 정보 수정")
    public ResVo putUserDog(@RequestBody @Valid UpdUserDogDto dto){
        return service.updUserDog(dto);
    }
    //유저 강아지 사진 수정
    @PatchMapping
    @Operation(summary = "유저 강아지 사진 수정",description = "포스트맨을 통해 테스트")
    public ResVo patchUserDogPic(@RequestPart MultipartFile pic
                                 ,@RequestPart @Valid PatchUserDogPicDto dto){
        dto.setPic(pic);
        return service.updUserDogPic(dto);
    }
    //유저 강아지 삭제
    @DeleteMapping
    @Operation(summary = "유저 강아지 삭제",description = "유저 강아지 삭제")
    public ResVo delUserDog(@Valid DelUserDogDto dto){
        return service.delUserDog(dto);
    }

}

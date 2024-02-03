package com.green.hoteldog.dog;

import com.green.hoteldog.common.MyFileUtils;
import com.green.hoteldog.common.ResVo;
import com.green.hoteldog.dog.models.*;
import com.green.hoteldog.exceptions.AuthorizedErrorCode;
import com.green.hoteldog.exceptions.CommonErrorCode;
import com.green.hoteldog.exceptions.CustomException;
import com.green.hoteldog.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DogService {
    private final DogMapper mapper;
    private final MyFileUtils fileUtils;
    private final AuthenticationFacade facade;

    //유저가 등록한 강아지 리스트 호출
    public List<GetDogListVo> selUserDogList(GetUserDogDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        return mapper.selUserDog(dto);
    }

    //유저 강아지 등록
    @Transactional(rollbackFor = Exception.class)
    public ResVo insUserDog(InsUserDogDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        try {
            mapper.insUserDog(dto);
            if(dto.getDogPic() != null){
                String target = "/user/"+dto.getUserPk()+"/"+dto.getUserDogPk();
                String saveFileNm = fileUtils.transferTo(dto.getDogPic(),target);
                SetUserDogPicDto picDto = new SetUserDogPicDto();
                picDto.setPic(saveFileNm);
                picDto.setUserDogPk(dto.getUserDogPk());
                picDto.setUserPk(dto.getUserPk());
                mapper.setUserDogPic(picDto);
            }
            return new ResVo(1);
        }catch (Exception e){
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }

    }

    //유저 강아지 정보 수정
    public ResVo updUserDog(UpdUserDogDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        int result = mapper.updUserDog(dto);
        return new ResVo(result);
    }
    public ResVo updUserDogPic(PatchUserDogPicDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        if (dto.getUserPk() == 0){
            throw new CustomException(AuthorizedErrorCode.NOT_AUTHORIZED);
        }
        String target = "/user/"+facade.getLoginUserPk()+"/"+dto.getUserDogPk();
        fileUtils.delFolderTrigger(target);
        String saveFileNm = fileUtils.transferTo(dto.getPic(), target);
        SetUserDogPicDto picDto = new SetUserDogPicDto();
        picDto.setUserPk(facade.getLoginUserPk());
        picDto.setUserDogPk(dto.getUserDogPk());
        picDto.setPic(saveFileNm);
        try {
            mapper.setUserDogPic(picDto);
        }catch (Exception e){
            return new ResVo(0);
        }
        return new ResVo(1);


    }

    //유저 강아지 삭제
    public ResVo delUserDog(DelUserDogDto dto){
        dto.setUserPk(facade.getLoginUserPk());
        String target = "/user/" + dto.getUserPk();
        try {
            mapper.delUserDog(dto);
        }catch (Exception e){
            return new ResVo(0);
        }
        fileUtils.delFolderTrigger(target);
        return new ResVo(1);
    }
}

package com.green.hoteldog.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Getter
@Component
public class MyFileUtils {
    private final String uploadPrefixPath;

    public MyFileUtils(@Value("${file.dir}") String uploadPrefixPath) {
        this.uploadPrefixPath = uploadPrefixPath;
    }

    public String makeFolders(String path) {
        File folder = new File(uploadPrefixPath, path);
        folder.mkdirs();
        return folder.getAbsolutePath();
        //AbsolutePath : 절대주소, 풀 주소
    }

    //랜덤 파일명 만들기
    public String getRandomFileNm() {
        return UUID.randomUUID().toString();
    }

    //확장자 얻어오기
    public String getExt(String fileNm) {
        return fileNm.substring(fileNm.lastIndexOf("."));
    }

    public String getRandomFileNm(String originFileNm) {
        return getRandomFileNm() + getExt(originFileNm);
    }

    //랜덤 파일명 만들기 with 확장자 from MultipartFile
    public String getRandomFileNm(MultipartFile mf) {
        String fileNm = mf.getOriginalFilename();
        return getRandomFileNm(fileNm);
    }

    public String transferTo(MultipartFile mf, String target) {
        String fileNm = getRandomFileNm(mf);
        String folderPath = makeFolders(target);
        File saveFile = new File(folderPath, fileNm);
        try {
            mf.transferTo(saveFile);
            return fileNm;
        } catch (Exception e) {
            return null;
        }
    }

    public void delFolderTrigger(String relativePath) {//폴더 안 파일만 삭제
        delFiles(uploadPrefixPath + relativePath);
    }

    public void delFiles(String folderPath) {// 폴더 아래에 폴더 및 파일 삭제
        File folder = new File(folderPath);
        if (folder.exists()) {
            File[] files = folder.listFiles();

            for (File file : files) {
                file.delete();
            }

        }
    }

    public void delAllFolderTrigger(String relativePath) {//폴더 및 안의 파일 삭제
        delAllFiles(uploadPrefixPath + relativePath);
    }

    public void delAllFiles(String folderPath) {//입력받은 폴더안 파일만 삭제
        File folder = new File(folderPath);
        if (folder.exists()) {
            File[] files = folder.listFiles();

            for (File file : files) {//재귀호출

                if (file.isDirectory()) {
                    delAllFiles(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
            folder.delete();
        }
    }
}

package sst.community.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommunityFileController {

    // 이미지 업로드
    @PostMapping("/api/community/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        // uploads/community 경로
        String uploadDir =
                System.getProperty("user.dir")
                + "/uploads/community/";

        // 폴더 없으면 생성
        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // UUID 파일명 생성
        String originalName = file.getOriginalFilename();

        String ext =
                originalName.substring(
                        originalName.lastIndexOf("."));

        String saveName =
                UUID.randomUUID() + ext;

        // 실제 저장
        File saveFile =
                new File(uploadDir + saveName);

        file.transferTo(saveFile);

        // 프론트에 반환할 이미지 URL
        String imageUrl =
                "/uploads/community/" + saveName;

        return ResponseEntity.ok(imageUrl);
    }
}
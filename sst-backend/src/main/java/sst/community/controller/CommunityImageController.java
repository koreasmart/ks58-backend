package sst.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.community.service.CommunityImageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityImageController {

    private final CommunityImageService communityImageService;

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteImage(
    		@RequestParam("imageUrl") String imageUrl) {

        communityImageService.deleteImage(imageUrl);

        return ResponseEntity.ok().build();
    }
}
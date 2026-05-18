package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sst.content.dto.PlaceImgDto;
import sst.content.service.PlaceImgService;
import java.util.List;

@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class PlaceImgController {

    private final PlaceImgService placeImgService;

    // GET /api/place/{plcNo}/images
    @GetMapping("/{plcNo}/images")
    public ResponseEntity<List<PlaceImgDto>> getImages(
            @PathVariable("plcNo") Long plcNo) {
        return ResponseEntity.ok(placeImgService.getImages(plcNo));
    }
}
package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sst.content.dto.PlayResponseDto;
import sst.content.service.PlayService;
import java.util.List;

@RestController
@RequestMapping("/api/play")
@RequiredArgsConstructor
public class PlayController {

    private final PlayService playService;

    // GET /api/play/list?rgnCd=11
    @GetMapping("/list")
    public ResponseEntity<List<PlayResponseDto>> getList(
            @RequestParam(name = "rgnCd", required = false) Integer rgnCd) {
        return ResponseEntity.ok(playService.getListByRegion(rgnCd));
    }

    // GET /api/play/755
    @GetMapping("/{plcNo}")
    public ResponseEntity<PlayResponseDto> getDetail(
            @PathVariable(name = "plcNo") Long plcNo) {
        return ResponseEntity.ok(playService.getDetail(plcNo));
    }
}
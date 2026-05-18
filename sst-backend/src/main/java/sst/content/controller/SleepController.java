package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sst.content.dto.SleepResponseDto;
import sst.content.service.SleepService;
import java.util.List;

@RestController
@RequestMapping("/api/sleep")
@RequiredArgsConstructor
public class SleepController {

    private final SleepService sleepService;

    // GET /api/sleep/list?rgnCd=11
    @GetMapping("/list")
    public ResponseEntity<List<SleepResponseDto>> getList(
            @RequestParam(name = "rgnCd", required = false) Integer rgnCd) {
        return ResponseEntity.ok(sleepService.getListByRegion(rgnCd));
    }

    // GET /api/sleep/755
    @GetMapping("/{plcNo}")
    public ResponseEntity<SleepResponseDto> getDetail(
            @PathVariable(name = "plcNo") Long plcNo) {
        return ResponseEntity.ok(sleepService.getDetail(plcNo));
    }
}
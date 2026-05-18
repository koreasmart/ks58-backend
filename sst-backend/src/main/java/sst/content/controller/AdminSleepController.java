package sst.content.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.content.dto.SleepResponseDto;
import sst.content.dto.SleepUpdateRequestDto;
import sst.content.service.AdminSleepService;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.response.ApiResponse;

@RestController
@RequestMapping("/api/admin/sleep")
@RequiredArgsConstructor
public class AdminSleepController {

    private final AdminSleepService adminSleepService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PageResponse<SleepResponseDto>>> getList(
            @RequestParam(name = "rgnCd", required = false) Integer rgnCd,
            @RequestParam(name = "useYn", required = false, defaultValue = "Y") String useYn, // 🚀 프론트에서 넘어오는 상태 필터값 추가
            PageRequest pageRequest) { 

        // 🚀 Service로 useYn 전달
        PageResponse<SleepResponseDto> result = adminSleepService.getListPageByRegion(rgnCd, useYn, pageRequest);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 🚀 추가: 휴지통 이동 및 복구를 처리할 상태 변경(PATCH) API
    @PatchMapping("/{plcNo}/status")
    public ResponseEntity<ApiResponse<Void>> toggleStatus(
            @PathVariable("plcNo") Long plcNo,
            @RequestParam("useYn") String useYn) {
        
    	adminSleepService.updatePlaceUseYn(plcNo, useYn);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{plcNo}")
    public ResponseEntity<ApiResponse<SleepResponseDto>> getDetail(
            @PathVariable(name = "plcNo") Long plcNo) {
        // 🚀 3. 단건 조회도 ApiResponse 규격을 맞추어 프론트엔드 연동의 일관성을 유지합니다.
        return ResponseEntity.ok(ApiResponse.success(adminSleepService.getDetail(plcNo)));
    }
    
    /**
     * 잘거리 수정
     * PUT /api/admin/sleep/{plcNo}
     */
    @PutMapping("/{plcNo}")
    public ResponseEntity<ApiResponse<Void>> updateDetail(
            @PathVariable(name = "plcNo") Long plcNo,
            @RequestBody SleepUpdateRequestDto requestDto) {
        
        adminSleepService.updateSleep(plcNo, requestDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
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
import sst.content.dto.FoodResponseDto;
import sst.content.dto.FoodUpdateRequestDto;
import sst.content.service.AdminFoodService;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.response.ApiResponse;

@RestController
@RequestMapping("/api/admin/food")
@RequiredArgsConstructor
public class AdminFoodController {

    private final AdminFoodService adminFoodService;

    // 🚀 AdminFoodController, AdminSleepController, AdminPlayController 모두 동일하게 적용
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PageResponse<FoodResponseDto>>> getList(
            @RequestParam(name = "rgnCd", required = false) Integer rgnCd,
            @RequestParam(name = "useYn", required = false, defaultValue = "Y") String useYn, // 🚀 추가
            PageRequest pageRequest) { 
        
        PageResponse<FoodResponseDto> result = adminFoodService.getListPageByRegion(rgnCd, useYn, pageRequest); // 🚀 useYn 전달
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 🚀 추가: 휴지통 ↔ 운영중 토글 API
    @PatchMapping("/{plcNo}/status")
    public ResponseEntity<ApiResponse<Void>> toggleStatus(
            @PathVariable("plcNo") Long plcNo,
            @RequestParam("useYn") String useYn) {
        
        adminFoodService.updatePlaceUseYn(plcNo, useYn);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{plcNo}")
    public ResponseEntity<ApiResponse<FoodResponseDto>> getDetail(
            @PathVariable(name = "plcNo") Long plcNo) {
        return ResponseEntity.ok(ApiResponse.success(adminFoodService.getDetail(plcNo)));
    }
    
    /**
     * 먹거리 수정
     * PUT /api/admin/food/{plcNo}
     */
    @PutMapping("/{plcNo}")
    public ResponseEntity<ApiResponse<Void>> updateDetail(
            @PathVariable(name = "plcNo") Long plcNo,
            @RequestBody FoodUpdateRequestDto requestDto) {
        
        // 🚀 프론트엔드 공통 규격인 ApiResponse 포맷으로 통일하여 응답
        adminFoodService.updateFood(plcNo, requestDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    	
}
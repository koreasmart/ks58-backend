package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sst.content.dto.FoodResponseDto;
import sst.content.service.FoodService;

import java.util.List;


/**
 * 먹거리(Food) API 컨트롤러
 * - 클라이언트(React)의 HTTP 요청을 받아 서비스로 전달하고 응답을 반환
 * - Base URL: /api/food
 */

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    /**
     * 먹거리 목록 조회
     * GET /api/food/list?rgnCd=11
     * @param rgnCd 지역코드 (null이면 전체 조회)
     */
    
    @GetMapping("/list")
    public ResponseEntity<List<FoodResponseDto>> getList(
            @RequestParam(name = "rgnCd", required = false) Integer rgnCd) {
        return ResponseEntity.ok(foodService.getListByRegion(rgnCd));
    }

    /**
     * 먹거리 상세 조회
     * GET /api/food/755
     * @param plcNo 장소 고유번호 (PLC_NO)
     */
    
    @GetMapping("/{plcNo}")
    public ResponseEntity<FoodResponseDto> getDetail(
            @PathVariable(name = "plcNo") Long plcNo) {
        return ResponseEntity.ok(foodService.getDetail(plcNo));
    }
}
package sst.content.controller;

import lombok.RequiredArgsConstructor;
import sst.content.dto.SeeResponseDto;
import sst.content.service.SeeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 볼거리(See) API 컨트롤러
 * - 클라이언트(React)의 HTTP 요청을 받아 서비스로 전달하고 응답을 반환
 * - Base URL: /api/see
 */

@RestController
@RequestMapping("/api/see")
@RequiredArgsConstructor
public class SeeController {

    private final SeeService seeService;

    /**
     * 볼거리 목록 조회
     * GET /api/see/list?rgnCd=11
     * @param rgnCd 지역코드 (null이면 전체 조회)
     */
    
    // GET /api/see/list?rgnCd=11
    @GetMapping("/list")
    public ResponseEntity<List<SeeResponseDto>> getList(
            @RequestParam(name = "rgnCd", required = false) Integer rgnCd) {
        return ResponseEntity.ok(seeService.getListByRegion(rgnCd));
    }

    /**
     * 볼거리 상세 조회
     * GET /api/see/755
     * @param plcNo 장소 고유번호 (PLC_NO)
     */
    
    // GET /api/see/755
    @GetMapping("/{plcNo}")
    public ResponseEntity<SeeResponseDto> getDetail(
            @PathVariable(name = "plcNo") Long plcNo) {
        return ResponseEntity.ok(seeService.getDetail(plcNo));
    }
}
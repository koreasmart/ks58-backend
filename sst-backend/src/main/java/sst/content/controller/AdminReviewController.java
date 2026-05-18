package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sst.content.service.AdminReviewService;
import sst.global.dto.PageRequest;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    /**
     * 관리자 리뷰 목록 조회
     */
    @GetMapping
    public ResponseEntity<?> getReviews(
            // 🚀 name="tab"을 name="useYn"으로, 변수명도 useYn으로 변경!
            @RequestParam(name = "useYn", defaultValue = "Y") String useYn, 
            PageRequest pageRequest) {
        
        return ResponseEntity.ok(adminReviewService.getReviewsPaged(useYn, pageRequest));
    }

    /**
     * 리뷰 상태 토글 (휴지통 이동 및 복구, 블라인드 해제)
     */
    @PatchMapping("/{rvwNo}/status")
    public ResponseEntity<?> toggleStatus(
            @PathVariable("rvwNo") Long rvwNo,     // 🚀 ("rvwNo") 추가
            @RequestParam("useYn") String useYn) { // 🚀 ("useYn") 추가
        
        adminReviewService.toggleReviewStatus(rvwNo, useYn);
        return ResponseEntity.ok("상태가 성공적으로 변경되었습니다.");
    }
}
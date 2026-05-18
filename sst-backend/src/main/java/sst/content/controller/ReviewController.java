package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sst.content.dto.ReviewRequestDto;
import sst.content.dto.ReviewResponseDto;
import sst.content.service.ReviewService;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ─────────────────────────────────────────
    // 리뷰 목록 조회
    // GET /api/reviews?plcNo={plcNo}
    // ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> selectReviews(
            @RequestParam("plcNo") Long plcNo) {
        return ResponseEntity.ok(reviewService.selectReviews(plcNo));
    }

    // ─────────────────────────────────────────
    // 리뷰 등록
    // POST /api/reviews
    // ─────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ReviewResponseDto> insertReview(
            @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok(reviewService.insertReview(dto));
    }

    // ─────────────────────────────────────────
    // 리뷰 수정
    // PUT /api/reviews/{rvwNo}
    // ─────────────────────────────────────────
    @PutMapping("/{rvwNo}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable("rvwNo") Long rvwNo,
            @RequestBody ReviewRequestDto dto) {
        dto.setRvwNo(rvwNo);
        return ResponseEntity.ok(reviewService.updateReview(dto));
    }

    // ─────────────────────────────────────────
    // 리뷰 삭제
    // DELETE /api/reviews/{rvwNo}
    // ─────────────────────────────────────────
    @DeleteMapping("/{rvwNo}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("rvwNo") Long rvwNo,
            @RequestBody ReviewRequestDto dto) {
        dto.setRvwNo(rvwNo);
        reviewService.deleteReview(dto);
        return ResponseEntity.noContent().build();
    }
}
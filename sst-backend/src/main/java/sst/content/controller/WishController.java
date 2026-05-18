package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sst.content.dto.WishDto;
import sst.content.dto.WishResponseDto;
import sst.content.service.WishService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    // ─────────────────────────────────────────
    // 찜 토글 (추가/해제)
    // POST /api/wishlist/toggle
    // ─────────────────────────────────────────
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Boolean>> toggleWish(
            @RequestBody WishDto dto) {
        boolean isWished = wishService.toggleWish(dto);
        return ResponseEntity.ok(Map.of("isWished", isWished));
    }

    // ─────────────────────────────────────────
    // 찜 여부 확인
    // GET /api/wishlist/check?mbrId={mbrId}&plcNo={plcNo}
    // ─────────────────────────────────────────
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkWish(
            @RequestParam("mbrId") Long mbrId,
            @RequestParam("plcNo") Long plcNo) {
        WishDto dto = new WishDto();
        dto.setWishMbrId(mbrId);
        dto.setWishPlcNo(plcNo);
        boolean isWished = wishService.checkWish(dto);
        return ResponseEntity.ok(Map.of("isWished", isWished));
    }

    // ─────────────────────────────────────────
    // 내 찜 목록 조회
    // GET /api/wishlist?mbrId={mbrId}
    // ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getMyWishlist(
            @RequestParam("mbrId") Long mbrId) {
        return ResponseEntity.ok(wishService.getMyWishlist(mbrId));
    }
}
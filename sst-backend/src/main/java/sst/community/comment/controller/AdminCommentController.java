package sst.community.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.community.comment.service.AdminCommentService;
import sst.global.dto.PageRequest;
import sst.global.security.domain.CustomUserDetails;

@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getComments(
            // 🚀 추가: SecurityContext에 저장된 유저 정보를 인자로 바로 받음 (DB 재조회 방지)
            @AuthenticationPrincipal CustomUserDetails userDetails, 
            @RequestParam(name = "useYn", defaultValue = "Y") String useYn, 
            PageRequest pageRequest) {
        
        // 🚀 수정: 서비스 호출 시 유저 정보를 넘기거나, 단순 목록 조회라면 유저 정보 없이 처리
        return ResponseEntity.ok(adminCommentService.getCommentsPaged(useYn, pageRequest));
    }

    @PatchMapping("/{cmtNo}/status")
    public ResponseEntity<?> toggleStatus(
            @PathVariable("cmtNo") Long cmtNo, 
            @RequestParam("useYn") String useYn) {
        adminCommentService.toggleCommentStatus(cmtNo, useYn);
        return ResponseEntity.ok("상태가 성공적으로 변경되었습니다.");
    }
}
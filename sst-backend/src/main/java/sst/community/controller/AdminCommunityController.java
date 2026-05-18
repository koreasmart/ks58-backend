package sst.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import sst.community.domain.Community;
import sst.community.service.AdminCommunityService;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.response.ApiResponse;

@RestController
@RequestMapping("/api/admin/community")
@RequiredArgsConstructor
public class AdminCommunityController {

    private final AdminCommunityService adminCommunityService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PageResponse<Community>>> getAdminCommunityList(
            @RequestParam("catCd") String catCd,
            @RequestParam("useYn") String useYn,
            PageRequest pageRequest) {
        PageResponse<Community> result = adminCommunityService.getAdminCommunityListPaged(catCd, useYn, pageRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 🚀 [추가] 관리자 글 수정을 위한 단건 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{commNo}")
    public ResponseEntity<ApiResponse<Community>> getCommunityDetail(@PathVariable("commNo") Long commNo) {
        Community result = adminCommunityService.getCommunityDetail(commNo);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PreAuthorize("hasRole('ADMIN')") // 🚀 관리자 권한(ROLE_ADMIN)이 있는 유저만 요청 가능
    @PutMapping("/{commNo}")
    public ResponseEntity<ApiResponse<Void>> updateCommunity(
            @PathVariable("commNo") Long commNo,
            @RequestBody Community community) {
        
        community.setCommNo(commNo); // 🚀 URL 경로의 번호를 객체에 세팅
        adminCommunityService.modifyCommunityByAdmin(community);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{commNo}/status")
    public ResponseEntity<ApiResponse<Void>> updateCommunityStatus(
            @PathVariable("commNo") Long commNo,
            @RequestParam("useYn") String useYn) {
        adminCommunityService.updateCommunityStatus(commNo, useYn);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{commNo}")
    public ResponseEntity<ApiResponse<Void>> deleteCommunity(@PathVariable("commNo") Long commNo) {
        adminCommunityService.deleteCommunity(commNo);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
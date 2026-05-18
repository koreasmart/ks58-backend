package sst.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.response.ApiResponse;
import sst.member.domain.Member;
import sst.member.dto.AdminMemberCreateRequest;
import sst.member.dto.AdminMemberUpdateRequest;
import sst.member.service.AdminMemberService;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;
    
    /**
     * 🚀 관리자: 회원 목록 조회 (검색 및 페이징)
     * URL 예시: /api/admin/members?page=1&size=10&searchType=email&keyword=test&authCd=ROLE_ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Member>>> getMembers(
            PageRequest pageRequest,
            @RequestParam(value = "useYn", required = false) String useYn,
            @RequestParam(value = "authCd", required = false) String authCd) { // 🚀 1. authCd 파라미터 추가
        
        PageResponse<Member> result = adminMemberService.getMembersPaged(pageRequest, useYn, authCd);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 관리자: 회원 강제 탈퇴
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> withdrawMember(@PathVariable("memberId") Long memberId) {
        adminMemberService.withdrawMemberByAdmin(memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 관리자: 신규 회원 등록
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMember(@Valid @RequestBody AdminMemberCreateRequest request) {
        adminMemberService.createMemberByAdmin(request);
        return ResponseEntity.status(201).body(ApiResponse.success(null));
    }

    /**
     * 관리자: 기존 회원 수정
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> updateMember(
            @PathVariable("memberId") Long memberId,
            @RequestBody AdminMemberUpdateRequest request) {
        
        adminMemberService.updateMemberByAdmin(memberId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
 // 🚀 프론트엔드에서 axios.get(`/api/admin/members/${id}`) 호출을 받을 컨트롤러 엔드포인트 추가
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Member>> getMemberDetail(@PathVariable("memberId") Long memberId) {
        Member member = adminMemberService.getMemberDetail(memberId);
        return ResponseEntity.ok(ApiResponse.success(member));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{memberId}/status")
    public ResponseEntity<ApiResponse<Void>> toggleMemberStatus(
            @PathVariable("memberId") Long memberId,
            @RequestParam("useYn") String useYn) {
        
        adminMemberService.updateMemberStatus(memberId, useYn);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
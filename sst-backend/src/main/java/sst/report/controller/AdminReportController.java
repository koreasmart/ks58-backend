package sst.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.response.ApiResponse;
import sst.global.security.domain.CustomUserDetails;
import sst.report.dto.AdminReportResponseDto;
import sst.report.service.AdminReportService;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AdminReportResponseDto>>> getReports(
            @RequestParam(name = "statusCd", required = false) String statusCd,
            @RequestParam(name = "rptTypeCd", required = false) String rptTypeCd, 
            PageRequest pageRequest) { 
        return ResponseEntity.ok(ApiResponse.success(adminReportService.getReportsPaged(statusCd, rptTypeCd, pageRequest)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{rptNo}/status")
    public ResponseEntity<ApiResponse<String>> updateReportStatus( // 🚀 1. Void 대신 String을 반환하도록 수정
            @PathVariable("rptNo") Long rptNo,
            @RequestParam("statusCd") String statusCd, 
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        // 🚀 2. Service에서 반환하는 결과 메시지(자동 블라인드 여부 등)를 변수에 담습니다.
        String message = adminReportService.updateReportStatus(rptNo, statusCd, userDetails.getMember().getMbrId());
        
        // 🚀 3. ApiResponse에 이 메시지를 담아 프론트엔드로 전달합니다.
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
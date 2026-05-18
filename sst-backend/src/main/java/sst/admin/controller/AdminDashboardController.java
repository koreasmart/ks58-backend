package sst.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.admin.service.AdminDashboardService;
import sst.admin.dto.DashboardStatsResponse;
import sst.global.response.ApiResponse;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    /**
     * 관리자 대시보드 메인 통계 정보 조회
     */
    // 🚀 관리자 페이지이므로 반드시 관리자 권한을 가진 유저만 API를 호출할 수 있도록 인가(Authorization) 제어
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        
        DashboardStatsResponse stats = adminDashboardService.getDashboardStats();
        
        // 🚀 프로젝트 공통 규격인 ApiResponse.success() 로 감싸서 프론트엔드로 반환
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
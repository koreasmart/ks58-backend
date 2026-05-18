package sst.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.admin.dto.DashboardStatsResponse;
import sst.admin.mapper.AdminDashboardMapper;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AdminDashboardMapper adminDashboardMapper;
    
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        // 🚀 1. 기본 통계 카운트 가져오기
        DashboardStatsResponse stats = adminDashboardMapper.getDashboardStats();
        
        // 🚀 2. 최근 리스트 데이터 5건씩 가져와서 하나의 응답 객체(stats)에 조립
        stats.setRecentMembers(adminDashboardMapper.getRecentMembers());
        stats.setRecentPosts(adminDashboardMapper.getRecentPosts());
        
        return stats;
    }
}
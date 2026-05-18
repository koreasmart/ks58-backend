package sst.admin.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import sst.community.domain.Community;
import sst.member.domain.Member;

@Getter
@Setter
public class DashboardStatsResponse {
    // 🚀 프론트엔드 상태값 구조와 완벽히 일치하도록 네이밍
    private int totalMembers;
    private int newMembersToday;
    private int totalPosts;
    private int totalReports;
    
    private List<Member> recentMembers;
    private List<Community> recentPosts;
}
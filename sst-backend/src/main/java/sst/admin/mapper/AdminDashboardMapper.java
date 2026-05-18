package sst.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import sst.admin.dto.DashboardStatsResponse;
import sst.community.domain.Community;
import sst.member.domain.Member;

@Mapper
public interface AdminDashboardMapper {
    DashboardStatsResponse getDashboardStats();
    
    List<Member> getRecentMembers();
    List<Community> getRecentPosts();
}
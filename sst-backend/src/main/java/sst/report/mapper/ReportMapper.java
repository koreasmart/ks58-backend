package sst.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.report.domain.Report;
import sst.report.dto.AdminReportResponseDto;

@Mapper
public interface ReportMapper {

    // 신고 등록
    int insertReport(Report report);

    // 중복 신고 확인
    int countDuplicateReport(Report report);
    

    
    List<AdminReportResponseDto> findAllReportsPaged(
            @Param("statusCd") String statusCd, 
            @Param("rptTypeCd") String rptTypeCd, 
            @Param("searchType") String searchType, 
            @Param("keyword") String keyword, 
            @Param("offset") int offset, 
            @Param("size") int size);

    int countAllReports(
            @Param("statusCd") String statusCd, 
            @Param("rptTypeCd") String rptTypeCd, 
            @Param("searchType") String searchType, 
            @Param("keyword") String keyword);
    
    // 🚀 [추가] 1. 관리자: 신고 처리 상태 업데이트 (RST001 -> RST003 등)
    int updateReportStatus(@Param("rptNo") Long rptNo, 
                           @Param("statusCd") String statusCd, 
                           @Param("adminId") Long adminId);

    // 🚀 [추가] 2. 방금 처리된 신고 상세 조회 (어떤 대상을 신고했는지 확인용)
    Report findReportById(@Param("rptNo") Long rptNo);

    // 🚀 [추가] 3. 타겟별 유효한 누적 신고 수 조회 (반려 제외)
    int countValidReportsByTarget(Report report);

    // 🚀 [추가] 4. 누적 5회 이상 시 대상 비활성화(블라인드) 처리
    int blindReview(@Param("rvwNo") Long rvwNo);
    int blindCommunity(@Param("commNo") Long commNo);
    int blindComment(@Param("cmntNo") Long cmntNo);

    // 대상별 신고 누적 수 조회
    int countReportsByTarget(Report report);

    
}
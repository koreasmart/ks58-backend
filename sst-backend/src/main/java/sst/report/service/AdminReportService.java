package sst.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
// import sst.global.exception.CustomException; // 사용하지 않게 되어 제거
// import sst.global.exception.ErrorCode;
import sst.report.domain.Report;
import sst.report.dto.AdminReportResponseDto;
import sst.report.mapper.ReportMapper;

@Service
@RequiredArgsConstructor
public class AdminReportService {
    
    private final ReportMapper reportMapper;

    @Transactional(readOnly = true)
    public PageResponse<AdminReportResponseDto> getReportsPaged(String statusCd, String rptTypeCd, PageRequest pageRequest) {
        List<AdminReportResponseDto> list = reportMapper.findAllReportsPaged(
                statusCd, rptTypeCd, pageRequest.getSearchType(), pageRequest.getKeyword(), 
                pageRequest.getOffset(), pageRequest.getSize());
        int total = reportMapper.countAllReports(
                statusCd, rptTypeCd, pageRequest.getSearchType(), pageRequest.getKeyword());
        return new PageResponse<>(list, total, pageRequest);
    }

    // 🚀 [수정] 중복되는 역할을 하던 processReport() 메서드는 삭제했습니다.
    // 어차피 Controller에서는 updateReportStatus() 하나만 호출하면 됩니다.
    
    @Transactional
    public String updateReportStatus(Long rptNo, String statusCd, Long adminId) {
        // 1. 신고 처리 상태 업데이트
        reportMapper.updateReportStatus(rptNo, statusCd, adminId);

        // 2. 방금 처리한 신고 정보 조회 (타겟 식별)
        Report report = reportMapper.findReportById(rptNo);

        // 3. 해당 타겟의 누적 유효 신고수 카운트
        int reportCount = reportMapper.countValidReportsByTarget(report);

        // 4. 누적 5회 이상이면 해당 원문 비활성화(블라인드) 처리
        if (reportCount >= 5) {
            if ("RPT001".equals(report.getRptTypeCd())) {
                reportMapper.blindReview(report.getRptReviewNo());
            } else if ("RPT002".equals(report.getRptTypeCd())) {
                reportMapper.blindCommunity(report.getRptCommNo());
            } else if ("RPT003".equals(report.getRptTypeCd())) {
                reportMapper.blindComment(report.getRptCmntNo());
            }
            return "상태가 변경되었으며, 누적 신고 5회 이상으로 원문이 자동 블라인드(비활성화) 되었습니다.";
        }

        return "상태가 성공적으로 변경되었습니다.";
    }
}
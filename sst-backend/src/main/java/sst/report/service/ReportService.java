package sst.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;
import sst.report.domain.Report;
import sst.report.dto.ReportRequest;
import sst.report.mapper.ReportMapper;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportMapper reportMapper;

    // 신고 등록
    @Transactional
    public int addReport(Long reporterNo, ReportRequest request) { // 🚀 Controller 규격에 맞춰 int로 반환 타입 변경

        Report report = new Report();

        report.setRptReporterNo(reporterNo);
        report.setRptTypeCd(request.getRptTypeCd());
        report.setRptReviewNo(request.getRptReviewNo());
        report.setRptCommNo(request.getRptCommNo());
        report.setRptCmntNo(request.getRptCmntNo());
        report.setRptReasonCd(request.getRptReasonCd());
        report.setRptReasonContent(request.getRptReasonContent());

        // 🚀 [수정] 일반 유저의 신고는 무조건 '접수(RST001)' 상태여야 합니다. 
        // 유저 본인이 처리자(ProsrNo)가 되는 비정상적인 권한 탈취 로직을 제거했습니다.
        report.setRptStatusCd("RST001");

        // 중복 신고 확인
        int duplicateCount = reportMapper.countDuplicateReport(report);
        if (duplicateCount > 0) {
            throw new CustomException(ErrorCode.ALREADY_REPORTED);
        }

        // 신고 데이터 DB 저장
        reportMapper.insertReport(report);

        // 🚀 [수정] 이전 Mapper 병합에서 합의된 'countValidReportsByTarget'을 사용합니다.
        int reportCount = reportMapper.countValidReportsByTarget(report);

        // 누적 5회 이상 시 자동 블라인드 처리 로직 유지
        if (reportCount >= 5) {
            if ("RPT001".equals(report.getRptTypeCd())) {
                reportMapper.blindReview(report.getRptReviewNo());
            } else if ("RPT002".equals(report.getRptTypeCd())) {
                reportMapper.blindCommunity(report.getRptCommNo());
            } else if ("RPT003".equals(report.getRptTypeCd())) {
                reportMapper.blindComment(report.getRptCmntNo());
            }
        }

        return 1; // 🚀 Controller에서 1(성공)을 응답받아 처리할 수 있도록 리턴
    }
}
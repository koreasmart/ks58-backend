// src/main/java/sst/report/dto/AdminReportResponseDto.java
package sst.report.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AdminReportResponseDto {
    
    // 🚀 프론트엔드의 테이블 컬럼과 1:1로 매칭되는 필드들입니다.
    private Long rptNo;                 // 신고 번호
    private String rptTypeCd;           // 대상 유형 (RPT001: 리뷰, RPT002: 커뮤니티, RPT003: 댓글)
    private String rptReasonCd;         // 신고 사유 코드
    private String rptReasonContent;    // 기타 사유 상세 내용
    private String rptStatusCd;         // 처리 상태 (RST001: 접수, RST003: 완료 등)
    
    // 🚀 프론트엔드에서 Date 객체로 변환하기 쉽도록 표준 포맷 지정
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rptRegDate;   // 등록일
    
    // 🚀 테이블 조인(MEMBER)을 통해 가져온 데이터
    private String reporterName;        // 신고자 닉네임
    
    // 🚀 COALESCE 함수로 합쳐서 가져온 신고 대상의 실제 PK (나중에 '해당 글/리뷰로 이동하기' 기능 만들 때 씁니다)
    private Long targetId;        
    
    // 🚀 새로 추가: 신고당한 원본 글의 내용
    private String reportedContent;
    
    // 🚀 새로 추가: 원본 글 제목 (커뮤니티 글인 경우에만 존재)
    private String reportedTitle;
    
    private String reportedCategory;
    
}
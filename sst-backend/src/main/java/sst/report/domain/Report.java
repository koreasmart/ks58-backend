package sst.report.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Report {

    private Long rptNo;
    private Long rptReporterNo;
    private String rptTypeCd;
    private Long rptReviewNo;
    private Long rptCommNo;
    private Long rptCmntNo;
    private String rptReasonCd;
    private String rptReasonContent;
    private String rptStatusCd;
    private Long rptProsrNo;
    private LocalDateTime rptProsrDate;
    private LocalDateTime rptRegDate;
}
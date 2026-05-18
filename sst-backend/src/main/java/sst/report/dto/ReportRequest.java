package sst.report.dto;

import lombok.Data;

@Data
public class ReportRequest {

    private String rptTypeCd;
    private Long rptReviewNo;
    private Long rptCommNo;
    private Long rptCmntNo;
    private String rptReasonCd;
    private String rptReasonContent;
}
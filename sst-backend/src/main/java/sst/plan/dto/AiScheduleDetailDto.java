package sst.plan.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AiScheduleDetailDto {
    private Long      aisNo;
    private String    aisSchdulName;
    private LocalDate aisBeginDate;
    private LocalDate aisEndDate;
    private Integer   aisTotDays;

    private Long      aisdNo;
    private Integer   aisdDayNo;
    private LocalDate aisdTravelDate;

    private Long      aispNo;
    private Long      aispPlcNo;
    private Integer   aispVisitOrdr;

    private String    plcName;
    private String    plcCatCd;
    private String    plcFltCd;
    private String    plcLat;
    private String    plcLot;
    private String    plcOverview;
    private String    plcMainImgUrl;
    
    private Long    aisRgnNo;
    private String  aisTheme1Cd;
    private String  aisTheme2Cd;
    private String  aisTheme3Cd;
    private String  rgnName;
    
    private String aisTheme1Name;
    private String aisTheme2Name;
    private String aisTheme3Name;
    
    private String plcCatName;  // 카테고리명
}
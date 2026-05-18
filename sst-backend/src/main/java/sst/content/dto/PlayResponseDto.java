package sst.content.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PlayResponseDto {
    // ── PLACE 테이블 공통 필드 ──────────────────
    private Long    plcNo;
    private String  plcId;
    private Integer plcRgnCd;
    private String  plcName;
    private String  plcAddr;
    private String  plcDaddr;
    private String  plcLat;
    private String  plcLot;
    private String  plcTelno;
    private String  plcHomepage;
    private String  plcMainImgUrl;
    private String  plcThumImgUrl;
    private String  plcOverview;
    private String  plcFltCd;
    private Double  plcAvgRating;   // ← 추가
    private Integer plcReviewCnt;   // ← 추가

    // ── PLACE_PLAY 전용 필드 ───────────────────
    private String    playInfocenter;
    private String    playParking;
    private String    playRestdate;
    private String    playUsetime;
    private LocalDate playEventStart;
    private LocalDate playEventEnd;
}
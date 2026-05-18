package sst.content.dto;

import lombok.Data;

@Data
public class SleepResponseDto {
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
    private Double  plcAvgRating;
    private Integer plcReviewCnt;
    
    // ── PLACE_SLEEP 전용 필드 ───────────────────
    private String  sleepCheckIn;
    private String  sleepCheckOut;
    private String  sleepInfocenter;
    private String  sleepParking;
    private String  sleepReservation;
    private String  sleepReservationUrl;
    private String  sleepSubFacility;
    // 부대시설 (0/1)
    private Integer sleepBarbecu;
    private Integer sleepBeauty;
    private Integer sleepBeverage;
    private Integer sleepBicycle;
    private Integer sleepCampfire;
    private Integer sleepFitness;
    private Integer sleepKaraoke;
    private Integer sleepPublicBath;
    private Integer sleepPublicPc;
    private Integer sleepSauna;
    private Integer sleepSeminar;
    private Integer sleepSports;
}
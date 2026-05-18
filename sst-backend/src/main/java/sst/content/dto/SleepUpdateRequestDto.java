package sst.content.dto;

import lombok.Data;

@Data
public class SleepUpdateRequestDto {
    // 🚀 PLACE 테이블 공통 컬럼
    private String plcName;
    private String plcAddr;
    private String plcDaddr;
    private String plcTelno;
    private String plcHomepage;
    private String plcOverview;
    
    // 🚀 PLACE_SLEEP 테이블 기본 상세 컬럼
    private String sleepCheckIn;
    private String sleepCheckOut;
    private String sleepInfocenter;
    private String sleepParking;
    private String sleepReservation;
    private String sleepReservationUrl;
    private String sleepSubFacility;
    
    // 🚀 PLACE_SLEEP 테이블 부대시설 여부 (프론트에서 0 또는 1로 넘김)
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
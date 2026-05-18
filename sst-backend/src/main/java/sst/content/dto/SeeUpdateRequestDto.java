package sst.content.dto;

import java.util.List;

import lombok.Data;

@Data
public class SeeUpdateRequestDto {
    // 🚀 PLACE 테이블 공통 컬럼
    private String plcName;
    private String plcAddr;
    
    // 🚀 PLACE_SEE 테이블 상세 컬럼
    private String seeInfocenter;
    private String seeParking;
    private String seeRestdate;
    private String seeUsetime;
    
    private String plcDaddr;     // 상세주소
    private String plcTelno;     // 전화번호
    private String plcHomepage;  // 홈페이지 주소
    private String plcOverview;  // 장소 소개/개요
    private String plcFltCd;     // 장소 필터 코드
    private String plcLat;       // 위도 (DB Decimal 타입과 호환)
    private String plcLot;       // 경도 (DB Decimal 타입과 호환)
    
    private List<String> tagCodes;
}
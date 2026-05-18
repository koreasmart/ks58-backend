package sst.content.dto;

import lombok.Data;

/**
 * 먹거리 응답 DTO
 * - PLACE 테이블과 PLACE_FOOD 테이블을 JOIN한 결과를 담는 객체
 * - Controller → 클라이언트(React)로 전달되는 데이터 형식
 */

@Data
public class FoodResponseDto {
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
    // ✅ PLACE_FOOD 전용 필드
    private String  foodOpeningHours;
    private String  foodParking;
    private String  foodRestdate;
    private String  foodMenu;
    private String  foodInfocenter;
}
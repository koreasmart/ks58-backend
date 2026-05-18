package sst.content.dto;

import java.util.List;

import lombok.Data;

/**
 * 볼거리 응답 DTO
 * - PLACE 테이블과 PLACE_SEE 테이블을 JOIN한 결과를 담는 객체
 * - Controller → 클라이언트(React)로 전달되는 데이터 형식
 */

@Data
public class SeeResponseDto {
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
    private String  seeInfocenter;
    private String  seeParking;
    private String  seeRestdate;
    private String  seeUsetime;
    private Double  plcAvgRating;   // ← 추가
    private Integer plcReviewCnt;   // ← 추가
    private List<String> tagCodes;
}
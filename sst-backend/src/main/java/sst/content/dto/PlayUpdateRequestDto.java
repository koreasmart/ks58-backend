package sst.content.dto;

import lombok.Data;

@Data
public class PlayUpdateRequestDto {
    // 🚀 PLACE 테이블 공통 컬럼
    private String plcName;
    private String plcAddr;
    private String plcDaddr;
    private String plcTelno;
    private String plcHomepage;
    private String plcOverview;
    
    // 🚀 PLACE_PLAY 테이블 상세 컬럼
    private String playInfocenter;
    private String playParking;
    private String playRestdate;
    private String playUsetime;
    
    // HTML5 date input에서 넘어오는 "YYYY-MM-DD" 포맷 처리를 위해 String 사용
    private String playEventStart; 
    private String playEventEnd;   
}
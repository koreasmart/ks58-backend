package sst.content.dto;

import lombok.Data;

@Data
public class FoodUpdateRequestDto {
    // 🚀 PLACE 테이블 공통 컬럼
    private String plcName;
    private String plcAddr;
    private String plcDaddr;
    private String plcTelno;
    private String plcHomepage;
    private String plcOverview;
    
    // 🚀 PLACE_FOOD 테이블 상세 컬럼
    private String foodOpeningHours;
    private String foodParking;
    private String foodRestdate;
    private String foodMenu;
    private String foodInfocenter;
}
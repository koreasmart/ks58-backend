package sst.plan.dto;

import lombok.Data;

@Data
public class PlaceResponseDto {

    private Long         id;                // PLC_NO
    private String       name;              // PLC_NAME
    private Integer      regionCode;        // PLC_RGN_CD
    private String       regionName;        // RGN_NAME
    private String       placeCategory;     // PLC_CAT_CD
    private String       placeCategoryName; // CMM_CD_NAME (카테고리)
    private String       x;                 // PLC_LOT
    private String       y;                 // PLC_LAT
    private String       overview;          // PLC_OVERVIEW
    private String       placeFilter;       // PLC_FLT_CD
    private String       placeFilterName;   // CMM_CD_NAME (필터)
    private String 		 imgUrl;			// PLC_MAIN_IMG_URL
    private String		 placeThemeName; 	// GROUP_CONCAT으로 받기
}
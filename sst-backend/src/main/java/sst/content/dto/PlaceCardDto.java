package sst.content.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceCardDto {

    private Long plcNo;
    private String plcId;
    private String plcCatCd;
    private String plcName;
    private String plcOverview;
    private String plcMainImgUrl;
    
    private String plcAddr;
    private Double plcAvgRating;
    private Integer plcReviewCnt;
}
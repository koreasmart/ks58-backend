package sst.content.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WishResponseDto {
    private Long          wishNo;
    private Long          wishPlcNo;
    private LocalDateTime wishRegDate;
    private String        plcName;
    private String        plcAddr;
    private String        plcMainImgUrl;
    private String        plcFltCd;
    private String        plcCatCd;       // 카테고리 코드 (PLC001~PLC004)
    private Double        plcAvgRating;
    private Integer       plcReviewCnt;
    private String 		  rgnName;
}
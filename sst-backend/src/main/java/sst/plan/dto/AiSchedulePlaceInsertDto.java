package sst.plan.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiSchedulePlaceInsertDto {

    private Long    aisdNo;      // AI일정날짜번호
    private Long    plcNo;       // 장소번호
    private Integer visitOrder;  // 방문순서
}
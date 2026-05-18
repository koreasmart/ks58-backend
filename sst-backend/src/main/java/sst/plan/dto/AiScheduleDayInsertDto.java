package sst.plan.dto;

import lombok.Data;

@Data
public class AiScheduleDayInsertDto {

    private Long    aisdNo;      // AI일정날짜번호 (INSERT 후 자동 생성)
    private Long    aisNo;       // AI일정번호
    private String  travelDate;  // 여행날짜
    private Integer dayNo;       // 몇째날
}
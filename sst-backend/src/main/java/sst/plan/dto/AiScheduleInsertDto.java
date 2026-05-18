package sst.plan.dto;

import lombok.Data;

@Data
public class AiScheduleInsertDto {

    private Long    aisNo;        // AI일정번호 (INSERT 후 자동 생성)
    private Long    mbrId;        // 회원번호
    private String  scheduleName; // 일정명
    private String  startDate;    // 시작일
    private String  endDate;      // 종료일
    private Integer totalDays;    // 총 일수
    private Long    rgnNo;        // 지역번호
    private String  theme1;       // 테마1코드
    private String  theme2;       // 테마2코드
    private String  theme3;       // 테마3코드
}
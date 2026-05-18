package sst.community.life.dto;

import lombok.Data;

@Data
public class LifeScheduleDto {
    private Long aisNo;
    private String aisSchdulName;
    private String aisBeginDate;
    private String aisEndDate;
    private Integer aisTotDays;
}
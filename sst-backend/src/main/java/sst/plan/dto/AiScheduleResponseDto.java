package sst.plan.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AiScheduleResponseDto {
    private Long          aisNo;
    private String        aisSchdulName;
    private LocalDate     aisBeginDate;
    private LocalDate     aisEndDate;
    private Integer       aisTotDays;
    private String        aisRlsYn;
    private LocalDateTime aisRegDate;
}
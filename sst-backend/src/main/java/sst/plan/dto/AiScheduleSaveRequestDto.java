	package sst.plan.dto;
	
	import java.util.List;
	
	import lombok.Data;
	import lombok.Getter;
	import lombok.Setter;
	
	@Data
	public class AiScheduleSaveRequestDto {
	
	    private String       scheduleName; // 일정명
	    private String       startDate;    // 시작일
	    private String       endDate;      // 종료일
	    private Integer      totalDays;    // 총 일수
	    private Long         rgnNo;        // 지역번호
	    private String       rgnName;      // 지역명 추가
	    private String       theme1;       // 테마1
	    private String       theme2;       // 테마2
	    private String       theme3;       // 테마3
	    private List<String> themes;       // 테마 목록 추가
	    private List<DayDto> schedule;     // 일정 목록
	
	    @Getter
	    @Setter
	    public static class DayDto {
	        private Integer       day;
	        private List<PlanDto> plans;
	    }
	
	    @Getter
	    @Setter
	    public static class PlanDto {
	        private Long    placeId;
	        private Integer visitOrder;
	    }
	}
package sst.plan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.plan.dto.AiScheduleDayInsertDto;
import sst.plan.dto.AiScheduleDetailDto;
import sst.plan.dto.AiScheduleInsertDto;
import sst.plan.dto.AiSchedulePlaceInsertDto;
import sst.plan.dto.AiScheduleResponseDto;
import sst.plan.dto.PlaceResponseDto;

@Mapper
public interface AiPlanMapper {

    // 지역코드 + 테마코드 목록으로 장소 조회
    List<PlaceResponseDto> findPlacesByRegionAndThemes(
        @Param("rgnCd")      Integer      rgnCd,
        @Param("themeCodes") List<String> themeCodes
    );

    // 지역명 -> 지역코드 조회
    Integer findRgnCdByRgnName(@Param("rgnName") String rgnName);

    // 테마명 -> 테마코드 목록 조회
    List<String> findThemeCodesByNames(@Param("themeNames") List<String> themeNames);

    // 장소별 태그 조회
    List<String> findThemeCodesByPlcNo(@Param("plcNo") Long plcNo);

    // 태그코드 -> 태그명 목록 조회
    List<String> findThemeNamesByCodes(@Param("themeCodes") List<String> themeCodes);
    
    // AI 일정 저장
    void insertAiSchedule(AiScheduleInsertDto dto);

    // AI 일정 날짜별 저장
    void insertAiScheduleDay(AiScheduleDayInsertDto dto);

    // AI 일정 장소 저장
    void insertAiSchedulePlace(AiSchedulePlaceInsertDto dto);

    // 지역명 -> 지역번호 조회
    Long findRgnNoByRgnName(@Param("rgnName") String rgnName);
    
    // 내 일정 목록 조회
    List<AiScheduleResponseDto> selectMySchedules(@Param("mbrId") Long mbrId);
    
    List<AiScheduleDetailDto> selectScheduleDetail(@Param("aisNo") Long aisNo);
    
    // 내 일정 수정
    void updateScheduleName(@Param("aisNo") Long aisNo, @Param("scheduleName") String scheduleName);
    
    // 내 일정 삭제
    void deleteSchedulePlaceByAisNo(@Param("aisNo") Long aisNo);
    void deleteScheduleDayByAisNo(@Param("aisNo") Long aisNo);
    void deleteSchedule(@Param("aisNo") Long aisNo);
    
    // 일정 복사용 원본 조회
    AiScheduleInsertDto selectScheduleForCopy(@Param("aisNo") Long aisNo);
    // 복사용 day 목록 조회
    List<AiScheduleDayInsertDto> selectScheduleDaysForCopy(@Param("aisNo") Long aisNo);
    // 복사용 place 목록 조회
    List<AiSchedulePlaceInsertDto> selectSchedulePlacesForCopy(@Param("aisdNo") Long aisdNo);
}
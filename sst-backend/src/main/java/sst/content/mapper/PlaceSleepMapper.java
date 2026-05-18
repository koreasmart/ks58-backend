package sst.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.content.dto.SleepResponseDto;
import sst.content.dto.SleepUpdateRequestDto;

@Mapper
public interface PlaceSleepMapper {
    List<SleepResponseDto> findByRegion(@Param("rgnCd") Integer rgnCd);
    
    // 🚀 1. 검색어(keyword) 파라미터 추가
    int countSleepListByRegion(
            @Param("rgnCd") Integer rgnCd, 
            @Param("keyword") String keyword
    );

    // 🚀 2. 검색어(keyword) 파라미터 추가
    List<SleepResponseDto> findSleepListPaged(
            @Param("rgnCd") Integer rgnCd, 
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("keyword") String keyword
    );

    SleepResponseDto findById(@Param("plcNo") Long plcNo);
    
    // 🚀 기존 PlaceSleepMapper에 아래 두 메서드 추가
    int updatePlace(@Param("plcNo") Long plcNo, @Param("dto") SleepUpdateRequestDto dto);
    
    int updatePlaceSleep(@Param("plcNo") Long plcNo, @Param("dto") SleepUpdateRequestDto dto);
    
    // 🚀 기존 메서드에 @Param("useYn") 추가
    int countSleepListByRegion(
            @Param("rgnCd") Integer rgnCd, 
            @Param("keyword") String keyword,
            @Param("useYn") String useYn);

    List<SleepResponseDto> findSleepListPaged(
            @Param("rgnCd") Integer rgnCd, 
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("keyword") String keyword,
            @Param("useYn") String useYn);

    // 🚀 추가: 상태 변경 업데이트 메서드
    int updatePlaceUseYn(@Param("plcNo") Long plcNo, @Param("useYn") String useYn);
    
}
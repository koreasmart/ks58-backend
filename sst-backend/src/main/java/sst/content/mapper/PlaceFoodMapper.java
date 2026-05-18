package sst.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.content.dto.FoodResponseDto;
import sst.content.dto.FoodUpdateRequestDto;

@Mapper
public interface PlaceFoodMapper {
    List<FoodResponseDto> findByRegion(@Param("rgnCd") Integer rgnCd);
    FoodResponseDto findById(@Param("plcNo") Long plcNo);
    
    int countFoodListByRegion(@Param("rgnCd") Integer rgnCd, @Param("keyword") String keyword);

    List<FoodResponseDto> findFoodListPaged(
            @Param("rgnCd") Integer rgnCd, 
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("keyword") String keyword
    );
    
    // 🚀 추가: PLACE 공통 테이블 업데이트
    int updatePlace(@Param("plcNo") Long plcNo, @Param("dto") FoodUpdateRequestDto dto);
    
    // 🚀 추가: PLACE_FOOD 상세 테이블 업데이트
    int updatePlaceFood(@Param("plcNo") Long plcNo, @Param("dto") FoodUpdateRequestDto dto);
    
    // 🚀 PlaceFoodMapper, PlaceSleepMapper, PlacePlayMapper 모두 동일하게 적용
    int countFoodListByRegion(
            @Param("rgnCd") Integer rgnCd, 
            @Param("keyword") String keyword,
            @Param("useYn") String useYn); // 🚀 추가

    List<FoodResponseDto> findFoodListPaged(
            @Param("rgnCd") Integer rgnCd, 
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("keyword") String keyword,
            @Param("useYn") String useYn); // 🚀 추가

    // 🚀 추가: 상태 변경 쿼리 매핑
    int updatePlaceUseYn(@Param("plcNo") Long plcNo, @Param("useYn") String useYn);
}
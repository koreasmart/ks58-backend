package sst.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.content.dto.PlayResponseDto;
import sst.content.dto.PlayUpdateRequestDto;

@Mapper
public interface PlacePlayMapper {
    List<PlayResponseDto> findByRegion(@Param("rgnCd") Integer rgnCd);
    PlayResponseDto findById(@Param("plcNo") Long plcNo);
    
    int countPlayListByRegion(@Param("rgnCd") Integer rgnCd, @Param("keyword") String keyword);
    
    List<PlayResponseDto> findPlayListPaged(
            @Param("rgnCd") Integer rgnCd, 
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("keyword") String keyword
    );
    
    // 🚀 추가: PLACE 공통 테이블 업데이트
    int updatePlace(@Param("plcNo") Long plcNo, @Param("dto") PlayUpdateRequestDto dto);
    
    // 🚀 추가: PLACE_PLAY 상세 테이블 업데이트
    int updatePlacePlay(@Param("plcNo") Long plcNo, @Param("dto") PlayUpdateRequestDto dto);
    
    int countPlayListByRegion(
            @Param("rgnCd") Integer rgnCd, 
            @Param("keyword") String keyword,
            @Param("useYn") String useYn); 
    
    List<PlayResponseDto> findPlayListPaged(
            @Param("rgnCd") Integer rgnCd, 
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("keyword") String keyword,
            @Param("useYn") String useYn);

    // 🚀 추가: 상태 변경 업데이트 메서드
    int updatePlaceUseYn(@Param("plcNo") Long plcNo, @Param("useYn") String useYn);
    
}
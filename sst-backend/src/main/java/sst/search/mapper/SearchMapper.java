package sst.search.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.community.domain.Community;
import sst.content.dto.PlaceCardDto;

@Mapper
public interface SearchMapper {

	// 🚀 3. @Param("keyword")를 사용해 XML의 #{keyword}에 값을 바인딩합니다.
    List<PlaceCardDto> selectPlacesByKeyword(@Param("keyword") String keyword);
 // 장소 검색
    int countPlacesByKeyword(@Param("keyword") String keyword);
    List<PlaceCardDto> selectPlacesByKeywordPaged(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    // 커뮤니티 검색
    int countCommunitiesByKeyword(@Param("keyword") String keyword);
    List<Community> selectCommunitiesByKeywordPaged(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    int countPlacesByKeyword(@Param("keyword") String keyword, @Param("category") String category);
    List<PlaceCardDto> selectPlacesByKeywordPaged(@Param("keyword") String keyword, @Param("category") String category, @Param("offset") int offset, @Param("size") int size);
    
}

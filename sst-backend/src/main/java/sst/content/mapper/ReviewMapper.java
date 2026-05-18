package sst.content.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sst.content.dto.ReviewRequestDto;
import sst.content.dto.ReviewResponseDto;

@Mapper
public interface ReviewMapper {

    /** 리뷰 목록 조회 (사용자) */
    List<ReviewResponseDto> selectReviews(Long plcNo);

    /** 리뷰 등록 (사용자) */
    int insertReview(ReviewRequestDto dto);

    /** 리뷰 수정 (사용자) */
    int updateReview(ReviewRequestDto dto);

    /** 리뷰 삭제 (사용자 - 소프트 딜리트) */
    int deleteReview(ReviewRequestDto dto);

    /** 평점 캐시 동기화 (공통) */
    int syncRatingCache(Long plcNo);
    
    // ==========================================
    // 🚀 관리자용 메서드 (admin 접두사 추가)
    // ==========================================
    
    /** 관리자: 리뷰 목록 페이징 조회 */
    List<ReviewResponseDto> adminFindReviewListPaged(
            @Param("offset") int offset, 
            @Param("size") int size, 
            @Param("keyword") String keyword, 
            @Param("useYn") String useYn,
            @Param("searchType") String searchType);
    
    /** 관리자: 리뷰 총 개수 조회 */
    int adminCountReviewList(
            @Param("keyword") String keyword, 
            @Param("useYn") String useYn,
            @Param("searchType") String searchType);
    
    /** 관리자: 리뷰 상태 변경 */
    int adminUpdateReviewUseYn(@Param("rvwNo") Long rvwNo, @Param("useYn") String useYn);
    
    /** 관리자: 리뷰 번호로 장소 번호 조회 (동기화용) */
    Long adminFindPlcNoByRvwNo(Long rvwNo);
}
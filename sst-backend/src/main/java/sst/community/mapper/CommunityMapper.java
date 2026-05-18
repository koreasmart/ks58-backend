package sst.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.community.domain.Community;
import sst.community.dto.CommunityFileMapDto;
import sst.community.dto.PlaceCategoryDto;
import sst.community.dto.PlaceDto;
import sst.community.dto.RegionDto;
import sst.community.dto.CommunityDto;
import sst.community.dto.CommunityFileDto;

@Mapper
public interface CommunityMapper {

    // 커뮤니티 게시글 목록 조회
    List<Community> selectCommunityList(
            @Param("catCd") String catCd,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            @Param("sortType") String sortType,
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 커뮤니티 게시글 총 개수 조회(일반 사용자 목록 카운트)
    int countCommunityList(
            @Param("catCd") String catCd,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword
    );

    // 커뮤니티 게시글 상세 조회
    Community selectCommunityDetail(Long commNo);

    // 커뮤니티 게시글 등록
    void insertCommunity(CommunityDto communityDto);

    // 게시글 수정
    int updateCommunity(CommunityDto communityDto);

    // 커뮤니티 게시글 삭제
    int deleteCommunity(Long commNo);

    // 커뮤니티 게시글 조회수 증가
    void updateViewCount(Long commNo);

    // 커뮤니티 게시글 좋아요 여부 확인
    int selectLikeCount(@Param("commNo") Long commNo, @Param("mbrId") Long mbrId);
    
    // 게시글 목록 좋아요 여부 조회
    List<Long> selectLikedCommunityNos(
            @Param("commNos") List<Long> commNos,
            @Param("mbrId") Long mbrId
    );

    // 커뮤니티 게시글 좋아요 추가
    int insertCommunityLike(@Param("commNo") Long commNo, @Param("mbrId") Long mbrId);

    // 커뮤니티 게시글 좋아요 삭제
    int deleteCommunityLike(@Param("commNo") Long commNo, @Param("mbrId") Long mbrId);

    // 커뮤니티 게시글 좋아요 수 증가
    int increaseLikeCount(Long commNo);

    // 커뮤니티 게시글 좋아요 수 감소
    int decreaseLikeCount(Long commNo);

    // 해시태그 존재 여부 확인
    Long selectHashtagNo(String tagName);

    // 해시태그 등록
    void insertHashtag(String tagName);

    // 게시글 해시태그 연결
    void insertCommunityHashtag(
            @Param("commNo") Long commNo,
            @Param("tagNo") Long tagNo
    );
    
    // 인기 해시태그 TOP5 조회
    List<String> selectPopularHashtags(@Param("catCd") String catCd);

    // 게시글 좋아요 전체 삭제
    void deleteCommunityLikes(Long commNo);

    // 게시글 댓글 전체 삭제
    void deleteCommentsByCommunity(Long commNo);

    // 게시글 파일 매핑 전체 삭제
    void deleteCommunityFileMaps(Long commNo);

    // 게시글 해시태그 전체 삭제
    void deleteCommunityHashtags(Long commNo);

    // 파일 정보 등록
    void insertFile(CommunityFileDto file);

    // 커뮤니티 파일 매핑 등록
    void insertCommunityFileMap(CommunityFileMapDto map);

    // 게시글 이미지 목록 조회
    List<String> selectCommunityImages(Long commNo);

    // 게시글 대표 이미지 조회
    String selectCommunityImageUrl(Long commNo);

    // 게시글 파일 번호 목록 조회
    List<Long> selectCommunityFileNos(Long commNo);

    // 파일 정보 삭제
    void deleteFiles(@Param("fileNos") List<Long> fileNos);

    // 지역 조회
    List<RegionDto> selectRegionList();

    // 카테고리 조회
    List<PlaceCategoryDto> selectPlaceCategoryList();

    // 구체적인 장소 조회
    List<PlaceDto> selectPlaceList(
            @Param("rgnCd") Integer rgnCd,
            @Param("catCd") String catCd
    );

    // 관리자: 뽐낼거리 전체 목록 조회
    List<Community> selectAdminCommunityListPaged(
            @Param("catCd") String catCd,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("keyword") String keyword
    );

    /* =================================================================
    🚀 [관리자] 뽐낼거리 커뮤니티 관리 (Admin 필수 포함)
    ================================================================= */
    
	 // 관리자: 뽐낼거리 페이징 목록 조회
	 List<Community> selectAdminCommunityListPaged(
	         @Param("catCd") String catCd,
	         @Param("useYn") String useYn,
	         @Param("keyword") String keyword,
	         @Param("offset") int offset,
	         @Param("size") int size
	 );
	
	 // 관리자: 뽐낼거리 페이징 총 개수 카운트
	 int countAdminCommunityList(
	         @Param("catCd") String catCd,
	         @Param("useYn") String useYn,
	         @Param("keyword") String keyword
	 );
	
	 // 관리자: 상태 변경 (휴지통/복구/블라인드)
	 int updateAdminCommunityStatus(
	         @Param("commNo") Long commNo,
	         @Param("useYn") String useYn
	 );
	 
	 int updateCommunityByAdmin(Community community);

}
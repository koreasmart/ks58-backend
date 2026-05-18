package sst.community.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sst.community.domain.Community;
import sst.community.dto.CommunityFileMapDto;
import sst.community.dto.PlaceCategoryDto;
import sst.community.dto.PlaceDto;
import sst.community.dto.RegionDto;
import sst.community.mapper.CommunityMapper;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.community.dto.CommunityDto;
import sst.community.dto.CommunityFileDto;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityMapper communityMapper;

    // 커뮤니티 게시글 목록 조회
    public PageResponse<Community> getCommunityList(
            String catCd,
            PageRequest pageRequest
    ) {
        List<Community> list = communityMapper.selectCommunityList(
                catCd,
                pageRequest.getSearchType(),
                pageRequest.getKeyword(),
                pageRequest.getSortType(),
                pageRequest.getOffset(),
                pageRequest.getSize()
        );

        int totalCount = communityMapper.countCommunityList(
                catCd,
                pageRequest.getSearchType(),
                pageRequest.getKeyword()
        );

        return new PageResponse<>(list, totalCount, pageRequest);
    }
    
    // 커뮤니티 게시글 상세 조회
    public Community getCommunityDetail(Long commNo) {
        Community community = communityMapper.selectCommunityDetail(commNo);

        if (community != null) {
            List<String> images = communityMapper.selectCommunityImages(commNo);
            community.setImages(images);
        }

        return community;
    }
    
    // 커뮤니티 게시글 등록
    public void createCommunity(CommunityDto communityDto, List<CommunityFileDto> files) {
    	
    	// 핫플거리는 대표 이미지 필수
    	if ("CMM002".equals(communityDto.getCommCatCd())
    	        && (communityDto.getCommMainImgUrl() == null
    	        || communityDto.getCommMainImgUrl().isBlank())) {
    	    throw new IllegalArgumentException("핫플거리는 사진을 1장 이상 등록해야 합니다.");
    	}

        // 게시글 등록
        communityMapper.insertCommunity(communityDto);

        // 해시태그 등록
        if (communityDto.getHashtags() != null) {

            for (String tagName : communityDto.getHashtags()) {
            	
            	tagName = normalizeTag(tagName);

            	if (tagName.isBlank()) {
            	    continue;
            	}

                Long tagNo =
                        communityMapper.selectHashtagNo(tagName);

                if (tagNo == null) {

                    communityMapper.insertHashtag(tagName);

                    tagNo =
                            communityMapper.selectHashtagNo(tagName);
                }

                communityMapper.insertCommunityHashtag(
                        communityDto.getCommNo(),
                        tagNo
                );
            }
        }

        // 파일 등록 + 파일 매핑
        if (files != null && !files.isEmpty()) {

            for (int i = 0; i < files.size(); i++) {

                CommunityFileDto file = files.get(i);

                communityMapper.insertFile(file);

                CommunityFileMapDto map =
                        new CommunityFileMapDto();

                map.setCommNo(communityDto.getCommNo());
                map.setFileNo(file.getFileNo());
                map.setSortOrdr(i);

                communityMapper.insertCommunityFileMap(map);
            }
        }
    }
    
    // 커뮤니티 게시글 수정
    public void modifyCommunity(CommunityDto communityDto, List<CommunityFileDto> files) {
    	
    	// 핫플거리는 수정 시에도 대표 이미지 필수
    	if (communityDto.getCommMainImgUrl() == null
    	        || communityDto.getCommMainImgUrl().isBlank()) {
    	    throw new IllegalArgumentException("핫플거리는 사진을 1장 이상 등록해야 합니다.");
    	}

        // 기존 대표 이미지 경로 조회
        String oldImageUrl = communityMapper.selectCommunityImageUrl(communityDto.getCommNo());

        // 기존 파일 번호 목록 조회
        List<Long> oldFileNos = communityMapper.selectCommunityFileNos(communityDto.getCommNo());

        // 기존 이미지와 새 이미지가 다르면 기존 실제 파일 삭제
        if (oldImageUrl != null 
                && !oldImageUrl.isBlank()
                && communityDto.getCommMainImgUrl() != null
                && !oldImageUrl.equals(communityDto.getCommMainImgUrl())) {

            String filePath = oldImageUrl.replace("/uploads/", "");
            java.io.File file = new java.io.File("uploads/" + filePath);

            if (file.exists()) {
                file.delete();
            }
        }

        // 게시글 제목/내용/대표이미지 수정
        communityMapper.updateCommunity(communityDto);

        // 기존 해시태그 연결 전체 삭제
        communityMapper.deleteCommunityHashtags(communityDto.getCommNo());

        // 새 해시태그 다시 연결
        if (communityDto.getHashtags() != null) {
        	for (String tagName : communityDto.getHashtags()) {
        		
        		tagName = normalizeTag(tagName);

        		if (tagName.isBlank()) {
        		    continue;
        		}

                Long tagNo = communityMapper.selectHashtagNo(tagName);

                if (tagNo == null) {
                    communityMapper.insertHashtag(tagName);
                    tagNo = communityMapper.selectHashtagNo(tagName);
                }

                communityMapper.insertCommunityHashtag(communityDto.getCommNo(), tagNo);
            }
        }

        // 기존 파일 매핑 삭제
        communityMapper.deleteCommunityFileMaps(communityDto.getCommNo());

        // 기존 FILE 테이블 삭제
        if (oldFileNos != null && !oldFileNos.isEmpty()) {
            communityMapper.deleteFiles(oldFileNos);
        }

        // 새 파일 정보 등록 + 매핑
        if (files != null && !files.isEmpty()) {

            for (int i = 0; i < files.size(); i++) {

                CommunityFileDto file = files.get(i);

                communityMapper.insertFile(file);

                CommunityFileMapDto map =
                        new CommunityFileMapDto();

                map.setCommNo(communityDto.getCommNo());
                map.setFileNo(file.getFileNo());
                map.setSortOrdr(i);

                communityMapper.insertCommunityFileMap(map);
            }
        }
    }
    
    public void removeCommunity(Long commNo) {

        // FILE 테이블 삭제용 파일 번호 목록 조회
        List<Long> fileNos = communityMapper.selectCommunityFileNos(commNo);

        // 좋아요 삭제
        communityMapper.deleteCommunityLikes(commNo);

        // 해시태그 연결 삭제
        communityMapper.deleteCommunityHashtags(commNo);

        // 파일 매핑 삭제
        communityMapper.deleteCommunityFileMaps(commNo);

        // FILE 테이블 삭제
        if (fileNos != null && !fileNos.isEmpty()) {
            communityMapper.deleteFiles(fileNos);
        }

        // 게시글은 실제 삭제하지 않고 COMM_USE_YN = 'N' 처리
        communityMapper.deleteCommunity(commNo);
    }
    
    // 커뮤니티 게시글 조회수 증가
    public void increaseViewCount(Long commNo) {
        communityMapper.updateViewCount(commNo);
    }
    
    // 커뮤니티 게시글 좋아요 처리
    public boolean toggleLike(Long commNo, Long mbrId) {
        // 이미 좋아요 눌렀는지 확인
        int likeCount = communityMapper.selectLikeCount(commNo, mbrId);
        // 좋아요 이미 눌렀으면 취소
        if (likeCount > 0) {
            communityMapper.deleteCommunityLike(commNo, mbrId);
            // 게시글 좋아요 수 감소
            communityMapper.decreaseLikeCount(commNo);
            return false;
        }
        // 좋아요 안 눌렀으면 추가
        communityMapper.insertCommunityLike(commNo, mbrId);
        // 게시글 좋아요 수 증가
        communityMapper.increaseLikeCount(commNo);
        return true;
    }
    
    // 커뮤니티 게시글 좋아요 여부 조회
    public boolean isLiked(Long commNo, Long mbrId) {
        return communityMapper.selectLikeCount(commNo, mbrId) > 0;
    }
    
    // 지역 조회
    public List<RegionDto> getRegionList() {
        return communityMapper.selectRegionList();
    }
    
    // 카테고리 조회
    public List<PlaceCategoryDto> getPlaceCategoryList() {
        return communityMapper.selectPlaceCategoryList();
    }
    
    // 구제척인 장소
    public List<PlaceDto> getPlaceList(Integer rgnCd, String catCd) {
        return communityMapper.selectPlaceList(rgnCd, catCd);
    }
    
    // 인기 해시태그 TOP5 조회
    public List<String> getPopularHashtags(String catCd) {
        return communityMapper.selectPopularHashtags(catCd);
    }
    
    // 게시글 목록 좋아요 여부 조회
    public List<Long> getLikedCommunityNos(List<Long> commNos, Long mbrId) {

        if (commNos == null || commNos.isEmpty()) {
            return List.of();
        }

        return communityMapper.selectLikedCommunityNos(commNos, mbrId);
    }
    
    // 해시태그 정리
    private String normalizeTag(String tagName) {
        if (tagName == null) {
            return "";
        }

        return tagName
                .trim()
                .replaceFirst("^#", "")
                .replaceAll("\\s+", "");
    }
}
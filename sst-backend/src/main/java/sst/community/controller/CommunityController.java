package sst.community.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sst.community.domain.Community;
import sst.community.dto.CommunityDto;
import sst.community.dto.CommunityFileDto;
import sst.community.dto.PlaceCategoryDto;
import sst.community.dto.PlaceDto;
import sst.community.dto.RegionDto;
import sst.community.service.CommunityService;
import sst.global.dto.FileUploadResult;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.files.storage.FileStorage;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommunityController {

    private final CommunityService communityService;
    private final FileStorage fileStorage;

    // 사용자가 선택한 카테고리에 맞는 게시판 목록 조회
    @GetMapping("/api/community")
    public PageResponse<Community> communityList(
            @RequestParam("catCd") String catCd,
            PageRequest pageRequest
    ) {
        return communityService.getCommunityList(catCd, pageRequest);
    }
    
    // 인기 해시태그 TOP5 조회
    @GetMapping("/api/community/popular-hashtags")
    public List<String> getPopularHashtags(
            @RequestParam("catCd") String catCd
    ) {
        return communityService.getPopularHashtags(catCd);
    }
    
    // 커뮤니티 게시글 상세 조회
    @GetMapping("/api/community/{commNo}")
    public Community communityDetail(@PathVariable("commNo") Long commNo) {
        return communityService.getCommunityDetail(commNo);
    }
    
    // 커뮤니티 게시글 조회수 증가 - 세션 기준 중복 방지
    @PutMapping("/api/community/{commNo}/view")
    public void increaseViewCount(
            @PathVariable("commNo") Long commNo,
            HttpSession session
    ) {
        String viewKey = "VIEWED_COMMUNITY_" + commNo;

        if (session.getAttribute(viewKey) == null) {
            communityService.increaseViewCount(commNo);
            session.setAttribute(viewKey, true);
        }
    }
    
    // 커뮤니티 게시글 좋아요 여부 조회
    @GetMapping("/api/community/{commNo}/like")
    public boolean isLiked(
            @PathVariable("commNo") Long commNo,
            @RequestParam("mbrId") Long mbrId
    ) {
        return communityService.isLiked(commNo, mbrId);
    }
    
    // 게시글 목록 좋아요 여부 조회
    @GetMapping("/api/community/likes")
    public List<Long> getLikedCommunityNos(
            @RequestParam("commNos") List<Long> commNos,
            @RequestParam("mbrId") Long mbrId
    ) {
        return communityService.getLikedCommunityNos(commNos, mbrId);
    }
    
    // 커뮤니티 게시글 등록
    @PostMapping("/api/community")
    public void createCommunity(@RequestBody CommunityDto communityDto) {
    	communityService.createCommunity(communityDto, null);
    }
    
    // 커뮤니티 게시글 등록 - test
    @PostMapping("/api/community1")
    public void createCommunity1(@RequestPart("community") CommunityDto dto,
            					 @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    	log.info("community: {}", dto);
    	files.forEach(file -> log.info("fileName: {}", file.getOriginalFilename()));
    	
    	List<FileUploadResult> result = fileStorage.setup("community")
											        .onSuccess(r -> log.info("파일 저장 성공: {}", r.getFilePath())) // 로그만
											        .onFailure(e -> log.error("파일 저장 실패", e))                  // 로그만
											        .storeAll(files);
    	result.forEach(file -> log.info("file: {}", file));
    	
    	
    }
    
   
    
    // 커뮤니티 게시글 등록 + 이미지 업로드
    @PostMapping(value = "/api/community/with-images", consumes = "multipart/form-data")
    public void createCommunityWithImages(
            @RequestPart("community") CommunityDto communityDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {

        List<CommunityFileDto> files = saveCommunityImages(images);

        if (!files.isEmpty()) {
            communityDto.setCommMainImgUrl(files.get(0).getFilePath());
        }

        communityService.createCommunity(communityDto, files);
    }
    
    // 커뮤니티 게시글 수정
    @PutMapping("/api/community/{commNo}")
    public void modifyCommunity(
            @PathVariable("commNo") Long commNo,
            @RequestBody CommunityDto communityDto) {

        communityDto.setCommNo(commNo);

        communityService.modifyCommunity(communityDto, null);
    }

    // 커뮤니티 게시글 수정 + 이미지 업로드
    @PutMapping(value = "/api/community/{commNo}/with-images", consumes = "multipart/form-data")
    public void modifyCommunityWithImages(
            @PathVariable("commNo") Long commNo,
            @RequestPart("community") CommunityDto communityDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {

        communityDto.setCommNo(commNo);

        List<CommunityFileDto> files = saveCommunityImages(images);

        if (!files.isEmpty()) {
            communityDto.setCommMainImgUrl(files.get(0).getFilePath());
        }

        communityService.modifyCommunity(communityDto, files);
    }
    
    // 커뮤니티 게시글 삭제
    @DeleteMapping("/api/community/{commNo}")
    public void removeCommunity(@PathVariable("commNo") Long commNo) {
        communityService.removeCommunity(commNo);
    }
    
    // 커뮤니티 게시글 좋아요 처리
    @PostMapping("/api/community/{commNo}/like")
    public boolean toggleLike(
            @PathVariable("commNo") Long commNo,
            @RequestParam("mbrId") Long mbrId) {

        return communityService.toggleLike(commNo, mbrId);
    }
    
    // 지역 조회
    @GetMapping("/api/regions")
    public List<RegionDto> getRegionList() {
        return communityService.getRegionList();
    }
    
    // 카테고리 조회
    @GetMapping("/api/place-categories")
    public List<PlaceCategoryDto> getPlaceCategoryList() {
        return communityService.getPlaceCategoryList();
    }
    
    // 구제적인 장소
    @GetMapping("/api/places")
    public List<PlaceDto> getPlaceList(
            @RequestParam("rgnCd") Integer rgnCd,
            @RequestParam("catCd") String catCd
    ) {
        return communityService.getPlaceList(rgnCd, catCd);
    }

    // 커뮤니티 이미지 저장
    private List<CommunityFileDto> saveCommunityImages(
            List<MultipartFile> images) throws IOException {

        List<CommunityFileDto> files = new ArrayList<>();

        if (images == null || images.isEmpty()) {
            return files;
        }

        File uploadDir =
                new File(System.getProperty("user.dir"),
                "uploads/community");

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (MultipartFile image : images) {

            if (image == null || image.isEmpty()) {
                continue;
            }

            String originalName = image.getOriginalFilename();

            String ext = "";

            if (originalName != null
                    && originalName.contains(".")) {

                ext = originalName.substring(
                        originalName.lastIndexOf(".") + 1);
            }

            String saveName =
                    UUID.randomUUID().toString()
                    + (ext.isBlank() ? "" : "." + ext);

            File saveFile = new File(uploadDir, saveName);

            image.transferTo(saveFile.getAbsoluteFile());

            CommunityFileDto file = new CommunityFileDto();

            file.setFileOrgNm(originalName);
            file.setFileSaveNm(saveName);
            file.setFilePath("/uploads/community/" + saveName);
            file.setFileExt(ext);
            file.setFileSize(image.getSize());
            file.setFileMimeType(image.getContentType());
            file.setFileType("IMAGE");

            files.add(file);
        }

        return files;
    }
}
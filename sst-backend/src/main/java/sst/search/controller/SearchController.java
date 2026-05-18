// 🚀 1. SearchController.java
package sst.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sst.global.response.ApiResponse;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.content.dto.PlaceCardDto;
import sst.community.domain.Community;
import sst.search.service.SearchService;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;


    // 🚀 커뮤니티(뽐낼거리) 검색 (해시태그 포함, 페이징 적용)
    @GetMapping("/communities")
    public ResponseEntity<ApiResponse<PageResponse<Community>>> searchCommunitiesPaged(
            @RequestParam("keyword") String keyword,
            PageRequest pageRequest) {
        
        PageResponse<Community> result = searchService.searchCommunitiesPaged(keyword, pageRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/places")
    public ResponseEntity<ApiResponse<PageResponse<PlaceCardDto>>> searchPlacesPaged(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "category", defaultValue = "ALL") String category, // 🚀 카테고리 파라미터 추가
            PageRequest pageRequest) {
        PageResponse<PlaceCardDto> result = searchService.searchPlacesPaged(keyword, category, pageRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
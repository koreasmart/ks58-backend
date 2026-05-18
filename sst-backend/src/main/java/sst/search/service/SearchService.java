// 🚀 2. SearchService.java
package sst.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.content.dto.PlaceCardDto;
import sst.community.domain.Community;
import sst.search.mapper.SearchMapper;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;

    @Transactional(readOnly = true)
    public PageResponse<Community> searchCommunitiesPaged(String keyword, PageRequest pageRequest) {
        // 🚀 해시태그 검색 시 '#'을 붙이고 검색하는 유저를 위해 '#' 제거 처리
        String cleanKeyword = (keyword == null) ? "" : keyword.replace("#", "").trim();

        int total = searchMapper.countCommunitiesByKeyword(cleanKeyword);
        List<Community> list = searchMapper.selectCommunitiesByKeywordPaged(
                cleanKeyword, pageRequest.getOffset(), pageRequest.getSize());

        return new PageResponse<>(list, total, pageRequest);
    }
    
    @Transactional(readOnly = true)
    public PageResponse<PlaceCardDto> searchPlacesPaged(String keyword, String category, PageRequest pageRequest) {
        String cleanKeyword = (keyword == null) ? "" : keyword.trim();
        int total = searchMapper.countPlacesByKeyword(cleanKeyword, category);
        List<PlaceCardDto> list = searchMapper.selectPlacesByKeywordPaged(cleanKeyword, category, pageRequest.getOffset(), pageRequest.getSize());
        return new PageResponse<>(list, total, pageRequest);
    }
}
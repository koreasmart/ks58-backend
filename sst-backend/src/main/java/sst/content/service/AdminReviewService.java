package sst.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sst.content.dto.ReviewResponseDto;
import sst.content.mapper.ReviewMapper;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReviewService {
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponseDto> getReviewsPaged(String useYn, PageRequest pageRequest) {
        // 🚀 변경된 admin 접두사 메서드 호출
        int total = reviewMapper.adminCountReviewList(pageRequest.getKeyword(), useYn, pageRequest.getSearchType());
        List<ReviewResponseDto> list = reviewMapper.adminFindReviewListPaged(
                pageRequest.getOffset(), pageRequest.getSize(), pageRequest.getKeyword(), useYn, pageRequest.getSearchType());
        
        return new PageResponse<>(list, total, pageRequest);
    }

    @Transactional
    public void toggleReviewStatus(Long rvwNo, String useYn) {
        // 1. 관리자 전용 상태 변경 메서드 호출
        reviewMapper.adminUpdateReviewUseYn(rvwNo, useYn);
        
        // 2. 동기화를 위해 장소 번호 조회 후 공통 캐시 동기화 메서드 호출
        Long plcNo = reviewMapper.adminFindPlcNoByRvwNo(rvwNo);
        if (plcNo != null) {
            reviewMapper.syncRatingCache(plcNo); 
        }
    }
}
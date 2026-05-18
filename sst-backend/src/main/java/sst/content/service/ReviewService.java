package sst.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sst.content.dto.ReviewRequestDto;
import sst.content.dto.ReviewResponseDto;
import sst.content.mapper.ReviewMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    // ─────────────────────────────────────────
    // 리뷰 목록 조회
    // ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> selectReviews(Long plcNo) {
        return reviewMapper.selectReviews(plcNo);
    }

    // ─────────────────────────────────────────
    // 리뷰 등록
    // ─────────────────────────────────────────
    @Transactional
    public ReviewResponseDto insertReview(ReviewRequestDto dto) {
        int result = reviewMapper.insertReview(dto);
        if (result == 0) {
            throw new RuntimeException("리뷰 등록에 실패했습니다.");
        }
        reviewMapper.syncRatingCache(dto.getRvwPlcNo());

        return ReviewResponseDto.builder()
                .rvwNo(dto.getRvwNo())
                .rvwPlcNo(dto.getRvwPlcNo())
                .rvwMbrId(dto.getRvwMbrId())
                .rvwRating(dto.getRvwRating())
                .rvwContent(dto.getRvwContent())
                .build();
    }

    // ─────────────────────────────────────────
    // 리뷰 수정
    // ─────────────────────────────────────────
    @Transactional
    public ReviewResponseDto updateReview(ReviewRequestDto dto) {
        int result = reviewMapper.updateReview(dto);
        if (result == 0) {
            throw new RuntimeException("수정 권한이 없거나 존재하지 않는 리뷰입니다.");
        }
        reviewMapper.syncRatingCache(dto.getRvwPlcNo());

        return ReviewResponseDto.builder()
                .rvwNo(dto.getRvwNo())
                .rvwPlcNo(dto.getRvwPlcNo())
                .rvwMbrId(dto.getRvwMbrId())
                .rvwRating(dto.getRvwRating())
                .rvwContent(dto.getRvwContent())
                .build();
    }

    // ─────────────────────────────────────────
    // 리뷰 삭제
    // ─────────────────────────────────────────
    @Transactional
    public void deleteReview(ReviewRequestDto dto) {
        int result = reviewMapper.deleteReview(dto);
        if (result == 0) {
            throw new RuntimeException("삭제 권한이 없거나 존재하지 않는 리뷰입니다.");
        }
        reviewMapper.syncRatingCache(dto.getRvwPlcNo());
    }
}
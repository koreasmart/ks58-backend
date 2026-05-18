package sst.content.dto;

import lombok.Getter;
import lombok.Setter;

// 프론트가 보내는 데이터 (등록/수정/삭제)

/**
 * 리뷰 요청 DTO
 * - 등록: rvwPlcNo, rvwMbrId, rvwRating, rvwContent
 * - 수정: rvwNo, rvwMbrId, rvwRating, rvwContent
 * - 삭제: rvwNo, rvwMbrId
 */
@Getter
@Setter
public class ReviewRequestDto {

    private Long    rvwNo;      // 리뷰번호 (수정/삭제 시 필요)
    private Long    rvwPlcNo;   // 장소번호 (등록 시 필요)
    private Long    rvwMbrId;   // 작성자 회원번호 (본인 확인용)
    private Double  rvwRating;  // 별점 (0.0 ~ 5.0)
    private String  rvwContent; // 리뷰 내용
}
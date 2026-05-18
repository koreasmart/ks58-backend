package sst.community.comment.dto;

import lombok.Data;

@Data
public class AdminCommentResponseDto {
	private Long cmntNo;
    private Long commNo;
    private String commTitle;    // 🚀 조인: 커뮤니티 원문 제목
    private Long cmntMbrId;
    private String nickname;     // 🚀 조인: 작성자 닉네임
    private String cmntContent;
    private String cmntRegDate;
    private String cmntUseYn;
}
package sst.community.comment.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comment {

    // 댓글 번호
    private Long cmntNo;

    // 게시글 번호
    private Long cmntCommNo;

    // 회원 번호
    private Long cmntMbrId;

    // 댓글 내용
    private String cmntContent;

    // 등록일
    private LocalDateTime cmntRegDate;

    // 수정일
    private LocalDateTime cmntUpDate;

    // 사용 여부
    private String cmntUseYn;

    // 회원 닉네임 (조인용)
    private String mbrNickname;
    
    
}
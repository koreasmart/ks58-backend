package sst.community.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.community.comment.domain.Comment;
import sst.community.comment.dto.AdminCommentResponseDto;

@Mapper
public interface CommentMapper {

    // 댓글 목록 조회
    List<Comment> getCommentList(@Param("commNo") Long commNo);

    // 댓글 등록
    int insertComment(Comment comment);

    // 댓글 수정
    int updateComment(Comment comment);

    // 댓글 삭제
    int deleteComment(Long cmntNo);

    // 댓글 단건 조회
    Comment getCommentById(Long cmntNo);

    // 댓글 수 증가
    int increaseCommentCount(Long commNo);

    // 댓글 수 감소
    int decreaseCommentCount(Long commNo);
    
    // ==========================================
    // 🚀 관리자용 메서드 (admin 접두사 추가)
    // ==========================================
    
    List<AdminCommentResponseDto> adminFindCommentListPaged(
            @Param("offset") int offset, 
            @Param("size") int size, 
            @Param("keyword") String keyword, 
            @Param("useYn") String useYn,
            @Param("searchType") String searchType);
            
    int adminCountCommentList(
            @Param("keyword") String keyword, 
            @Param("useYn") String useYn,
            @Param("searchType") String searchType);
            
    int adminUpdateCommentUseYn(
            @Param("cmtNo") Long cmtNo, 
            @Param("useYn") String useYn);

}
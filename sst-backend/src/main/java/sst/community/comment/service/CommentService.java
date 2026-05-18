package sst.community.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import sst.community.comment.domain.Comment;
import sst.community.comment.mapper.CommentMapper;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    // 댓글 목록 조회
    public List<Comment> getCommentList(Long commNo) {

        return commentMapper.getCommentList(commNo);
    }

    // 댓글 등록
    @Transactional
    public int addComment(Comment comment) {

        int result = commentMapper.insertComment(comment);

        if(result > 0) {
            commentMapper.increaseCommentCount(comment.getCmntCommNo());
        }

        return result;
    }

    // 댓글 수정
    public int modifyComment(Comment comment) {

        return commentMapper.updateComment(comment);
    }

    // 댓글 삭제
    @Transactional
    public int removeComment(Long cmntNo) {

        Comment comment = commentMapper.getCommentById(cmntNo);

        int result = commentMapper.deleteComment(cmntNo);

        if(result > 0) {
            commentMapper.decreaseCommentCount(comment.getCmntCommNo());
        }

        return result;
    }
}
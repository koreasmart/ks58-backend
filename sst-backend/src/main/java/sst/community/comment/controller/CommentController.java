package sst.community.comment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import sst.community.comment.domain.Comment;
import sst.community.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping("/{commNo}")
    public List<Comment> getCommentList(
            @PathVariable("commNo") Long commNo) {
        return commentService.getCommentList(commNo);
    }

    // 댓글 등록
    @PostMapping
    public int addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    // 댓글 수정
    @PutMapping("/{cmntNo}")
    public int modifyComment(
            @PathVariable("cmntNo") Long cmntNo,
            @RequestBody Comment comment) {

        comment.setCmntNo(cmntNo);

        return commentService.modifyComment(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/{cmntNo}")
    public int removeComment(
            @PathVariable("cmntNo") Long cmntNo) {

        return commentService.removeComment(cmntNo);
    }
}
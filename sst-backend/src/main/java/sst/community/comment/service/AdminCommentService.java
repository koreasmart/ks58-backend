package sst.community.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.community.comment.dto.AdminCommentResponseDto;
import sst.community.comment.mapper.CommentMapper;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;

@Service
@RequiredArgsConstructor
public class AdminCommentService {
    
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    // 🚀 반환 타입 변경
    public PageResponse<AdminCommentResponseDto> getCommentsPaged(String useYn, PageRequest pageRequest) {
        int total = commentMapper.adminCountCommentList(pageRequest.getKeyword(), useYn, pageRequest.getSearchType());
        
        // 🚀 리스트 타입 변경
        List<AdminCommentResponseDto> list = commentMapper.adminFindCommentListPaged(
                pageRequest.getOffset(), pageRequest.getSize(), pageRequest.getKeyword(), useYn, pageRequest.getSearchType());
        
        return new PageResponse<>(list, total, pageRequest);
    }

    @Transactional
    public void toggleCommentStatus(Long cmtNo, String useYn) {
        commentMapper.adminUpdateCommentUseYn(cmtNo, useYn);
    }
}
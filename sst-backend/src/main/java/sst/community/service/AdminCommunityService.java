package sst.community.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.community.domain.Community;
import sst.community.mapper.CommunityMapper;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AdminCommunityService {

    private final CommunityMapper communityMapper;

    @Transactional(readOnly = true)
    public PageResponse<Community> getAdminCommunityListPaged(String catCd, String useYn, PageRequest pageRequest) {
        int total = communityMapper.countAdminCommunityList(catCd, useYn, pageRequest.getKeyword());
        List<Community> list = communityMapper.selectAdminCommunityListPaged(
                catCd, 
                useYn, 
                pageRequest.getKeyword(), 
                pageRequest.getOffset(), 
                pageRequest.getSize()
        );
        return new PageResponse<>(list, total, pageRequest);
    }

    // 🚀 [추가] 관리자: 커뮤니티 단건 상세 조회 (수정 폼 데이터 바인딩용)
    @Transactional(readOnly = true)
    public Community getCommunityDetail(Long commNo) {
        return communityMapper.selectCommunityDetail(commNo);
    }

    @Transactional
    public void modifyCommunityByAdmin(Community community) {
        // 🚀 매퍼 호출 시 타입을 Community(Domain)로 정확히 전달합니다.
        int result = communityMapper.updateCommunityByAdmin(community);
        if (result == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
    }

    @Transactional
    public void updateCommunityStatus(Long commNo, String useYn) {
        communityMapper.updateAdminCommunityStatus(commNo, useYn);
    }

    @Transactional
    public void deleteCommunity(Long commNo) {
        communityMapper.deleteCommunity(commNo);
    }
}
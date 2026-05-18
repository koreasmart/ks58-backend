package sst.community.life.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sst.community.life.dto.LifePlaceDto;
import sst.community.life.dto.LifeScheduleDto;
import sst.community.life.mapper.CommunityLifeMapper;

@Service
@RequiredArgsConstructor
public class CommunityLifeService {

    private final CommunityLifeMapper communityLifeMapper;

    // 내 AI 일정 목록 조회
    public List<LifeScheduleDto> getMyScheduleList(Long mbrId) {
        return communityLifeMapper.selectMyScheduleList(mbrId);
    }
    
    // 선택한 AI 일정의 장소 목록 조회
    public List<LifePlaceDto> getSchedulePlaceList(Long aisNo) {
        return communityLifeMapper.selectSchedulePlaceList(aisNo);
    }
}

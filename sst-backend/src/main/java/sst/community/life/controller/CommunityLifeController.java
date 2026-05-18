package sst.community.life.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.community.life.dto.LifePlaceDto;
import sst.community.life.dto.LifeScheduleDto;
import sst.community.life.service.CommunityLifeService;

@RestController
@RequiredArgsConstructor
public class CommunityLifeController {

    private final CommunityLifeService communityLifeService;

    // 내 AI 일정 목록 조회
    @GetMapping("/api/community/life/schedules")
    public List<LifeScheduleDto> getMyScheduleList(
    		@RequestParam("mbrId") Long mbrId
    ) {
        return communityLifeService.getMyScheduleList(mbrId);
    }
    
    // 선택한 AI 일정의 장소 목록 조회
    @GetMapping("/api/community/life/schedules/{aisNo}/places")
    public List<LifePlaceDto> getSchedulePlaceList(
            @PathVariable("aisNo") Long aisNo
    ) {
        return communityLifeService.getSchedulePlaceList(aisNo);
    }
}
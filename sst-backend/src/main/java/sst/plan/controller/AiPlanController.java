package sst.plan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.global.security.domain.CustomUserDetails;
import sst.plan.dto.AiScheduleResponseDto;
import sst.plan.dto.AiScheduleSaveRequestDto;
import sst.plan.dto.PlaceResponseDto;
import sst.plan.service.AiPlanService;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiPlanController {

    private final AiPlanService aiPlanService;

    @GetMapping("/travel/list")
    public List<PlaceResponseDto> AiPlanResponse(
            @RequestParam("region") String region,
            @RequestParam("themes") String themes) {
        return aiPlanService.getTravelPlaces(region, themes);
    }

    @PostMapping("/schedule/save")
    public ResponseEntity<?> saveSchedule(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AiScheduleSaveRequestDto request) {
        Long mbrId = userDetails.getMember().getMbrId();
        aiPlanService.saveSchedule(mbrId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/schedule/my")
    public ResponseEntity<List<AiScheduleResponseDto>> getMySchedules(
            @RequestParam("mbrId") Long mbrId) {
        return ResponseEntity.ok(aiPlanService.getMySchedules(mbrId));
    }

    @GetMapping("/schedule/detail")
    public ResponseEntity<Map<String, Object>> getScheduleDetail(
            @RequestParam("aisNo") Long aisNo) {
        return ResponseEntity.ok(aiPlanService.getScheduleDetail(aisNo));
    }

    @PutMapping("/schedule/update")
    public ResponseEntity<?> updateSchedule(
            @RequestParam("aisNo") Long aisNo,
            @RequestBody AiScheduleSaveRequestDto request) {
        aiPlanService.updateSchedule(aisNo, request.getScheduleName(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/schedule/delete")
    public ResponseEntity<?> deleteSchedule(
            @RequestParam("aisNo") Long aisNo) {
        aiPlanService.deleteSchedule(aisNo);
        return ResponseEntity.noContent().build();
    }

    // 일정 복사 (내 일정으로 가져오기)
    @PostMapping("/schedule/copy")
    public ResponseEntity<?> copySchedule(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("aisNo") Long aisNo) {
        Long mbrId = userDetails.getMember().getMbrId();
        aiPlanService.copySchedule(aisNo, mbrId);
        return ResponseEntity.ok().build();
    }
}
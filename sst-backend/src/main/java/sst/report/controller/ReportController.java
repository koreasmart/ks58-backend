package sst.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import sst.global.security.domain.CustomUserDetails;
import sst.member.domain.Member;
import sst.report.dto.ReportRequest;
import sst.report.service.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    // 신고 등록
    @PostMapping
    public ResponseEntity<Integer> addReport( // 🚀 1. 반환 타입을 Boolean에서 Integer로 변경했습니다.
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReportRequest request) {

        Member member = userDetails.getMember();

        // 🚀 2. ReportService가 반환하는 int 타입(성공 시 1)에 맞추어 int 변수로 받습니다.
        int result = reportService.addReport(
                member.getMbrId(),
                request
        );

        // 🚀 3. 정상적으로 처리된 결과(1)를 HTTP 200 OK와 함께 반환합니다.
        return ResponseEntity.ok(result);
    }
}
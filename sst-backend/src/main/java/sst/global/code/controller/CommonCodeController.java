package sst.global.code.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.global.code.dto.CommonCodeRequest;
import sst.global.code.dto.CommonCodeResponse;
import sst.global.code.dto.CommonCodeSearchRequest;
import sst.global.code.dto.GroupCodeResponse;
import sst.global.code.dto.PageResponse;
import sst.global.code.service.CommonCodeService;
import sst.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/common-codes")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    // 코드 중복 확인
    @GetMapping("/check")
    public boolean checkCommonCodeDuplicate(
            @RequestParam String code
    ) {
        return commonCodeService.existsCode(code);
    }

    // 그룹코드 목록 조회
    @GetMapping("/groups")
    public List<GroupCodeResponse> getGroupCodeList() {
        return commonCodeService.getGroupCodeList();
    }

    // 페이징 공통코드 조회
    @GetMapping("/page")
    public PageResponse<CommonCodeResponse> getCommonCodePage(
            @ModelAttribute CommonCodeSearchRequest searchRequest
    ) {
        return commonCodeService.getCommonCodePage(searchRequest);
    }

    // 공통코드 전체 조회 또는 그룹별 조회
    @GetMapping
    public List<CommonCodeResponse> getCommonCodeList(
            @RequestParam(required = false) String groupCode
    ) {
        return commonCodeService.getCommonCodeList(groupCode);
    }

    // 공통코드 등록
    @PostMapping
    public void addCommonCode(@RequestBody CommonCodeRequest request) {
        commonCodeService.addCommonCode(request);
    }

    // 공통코드 수정
    @PutMapping("/{code}")
    public void modifyCommonCode(
            @PathVariable String code,
            @RequestBody CommonCodeRequest request
    ) {
        request.setCode(code);
        commonCodeService.modifyCommonCode(request);
    }

    // 사용 여부 변경
    @PatchMapping("/{code}/use-yn")
    public void changeUseYn(
            @PathVariable String code,
            @RequestParam String useYn
    ) {
        commonCodeService.changeUseYn(code, useYn);
    }
    
    // 🚀 그룹코드 선택 시 다음 생성될 코드 반환 API 추가
    @GetMapping("/next-code")
    public ResponseEntity<ApiResponse<String>> getNextCommonCode(
            @RequestParam("groupCode") String groupCode,
            @RequestParam("prefix") String prefix) {
        
        String nextCode = commonCodeService.getNextCommonCode(groupCode, prefix);
        // 🚀 프로젝트 규칙에 따라 ApiResponse로 감싸서 반환
        return ResponseEntity.ok(ApiResponse.success(nextCode));
    }
}
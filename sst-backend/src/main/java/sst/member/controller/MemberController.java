package sst.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sst.global.response.ApiResponse;
import sst.global.security.domain.CustomUserDetails;
import sst.member.domain.Member;
import sst.member.dto.MemberUpdateRequest;
import sst.member.dto.PasswordChangeRequest;
import sst.member.service.MemberService;

/* 수정함 */
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	

	//@PreAuthorize("hasRole('ADMIN')")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/{email}")
	public ResponseEntity<ApiResponse<Member>> getMemberInfoByEmail(@PathVariable(value="email") String email){
		
		Member member = memberService.getMemberInfoByEmail(email);
		
		return ResponseEntity.ok(ApiResponse.success(member));
	}
	
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<Member>> getMemberInfoByEmail(@AuthenticationPrincipal CustomUserDetails userDetails){
		
		return ResponseEntity.ok(ApiResponse.success(userDetails.getMember()));
	}
	
	/**
     * 내 정보 수정
     */
    @PostMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute MemberUpdateRequest request) {

        memberService.updateMemberInfo(userDetails.getMember().getMbrId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    // 비밀번호 변경(인증된 사용자만 접근 가능)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PasswordChangeRequest request) {
        
        String userEmail = userDetails.getMember().getMbrEmail();
        memberService.changePassword(userEmail, request);
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    /**
     * 🚀 회원 탈퇴
     */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response) {
        
        // SecurityContext에서 현재 로그인한 유저의 PK 추출 후 서비스로 전달
        memberService.withdrawMember(userDetails.getMember().getMbrId(), response);
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

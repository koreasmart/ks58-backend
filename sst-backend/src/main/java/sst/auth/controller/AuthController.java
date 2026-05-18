package sst.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sst.auth.dto.LoginRequest;
import sst.auth.dto.LoginResponse;
import sst.auth.dto.SignUpRequest;
import sst.auth.service.AuthService;
import sst.global.response.ApiResponse;
import sst.global.security.domain.CustomUserDetails;
import sst.member.domain.Member;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
	
	private final AuthService authService;

	/**
	 * 회원가입
	 * @param SignUpRequest 사용자 회원정보
	 * @return Member 회원정보
	 */
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Member>> signUp(@Valid @RequestBody SignUpRequest request){
		
		Member member = authService.addMember(request);
		
		return ResponseEntity.status(201)
							 .body(ApiResponse.created(member));
	}
	
	/**
	 * 로그인
	 * @param LoginRequest 사용자로그인 정보
	 * @param LoginResponse 로그인 후 사용자 정보
	 */
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response){
		LoginResponse loginResponse = authService.login(request, response);
		return ResponseEntity.ok(ApiResponse.success(loginResponse));
	}
	
	/**
	 * refresh token 재발급
	 * @param HttpServletRequest (RefreshToken를 포함한 쿠키 정보 추출) 
	 * @param HttpServletResponse (새 AccessToken 추가)
	 */
	@PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request,
                                        HttpServletResponse response) {
        authService.refresh(request, response);
        return ResponseEntity.noContent().build();
    }
	
	/**
	 * 로그아웃 — DB의 Refresh Token 삭제 + 쿠키 만료
     * @param userDetails    로그아웃할 회원의 정보 (@AuthenticationPrincipal에서 추출)
     * @param response 쿠키 삭제를 위한 응답 객체
	 */
	@PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response) {
    	Member member = userDetails.getMember();
    	authService.logout(member.getMbrId(), response);
        return ResponseEntity.noContent().build();
    }
	/**
     * 새로고침 시 인증 상태 유지 (Refresh Token 기반)
     * HTTP Method를 GET으로 수정하고, 응답 본문에 사용자 정보를 담아 반환
     */
	@GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginResponse>> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 🚀 SecurityContext에 저장된 유저 정보를 꺼내어 프론트엔드에 필요한 데이터만 응답
        Member member = userDetails.getMember();
        
        LoginResponse response = LoginResponse.builder()
							        		  .mbrId(member.getMbrId())
							                  .mbrEmail(member.getMbrEmail())
							                  .mbrName(member.getMbrName())
							                  .mbrNickname(member.getMbrNickname())
							                  .memberRole(member.getMbrAuthCd()) 
							                  .mbrProviderCd(member.getMbrProviderCd())
							                  .mbrProfileInfo(member.getProfileFile()) 
		                                      .build();
                
        return ResponseEntity.ok(ApiResponse.success(response));
    }
	
	/**
     * 닉네임 중복 확인
     * @param nickname 검사할 닉네임
     * @return 중복이면 true, 사용 가능하면 false 반환
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isDuplicate = authService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(ApiResponse.success(isDuplicate));
    }
    
    /**
     * 이메일 중복 확인
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam("email") String email) {
        // URL 쿼리 파라미터로 받은 이메일 검사
        boolean isDuplicate = authService.checkEmailDuplicate(email);
        return ResponseEntity.ok(ApiResponse.success(isDuplicate));
    }
}















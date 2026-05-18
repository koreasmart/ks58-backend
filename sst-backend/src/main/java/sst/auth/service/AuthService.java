package sst.auth.service;

import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import sst.auth.dto.LoginRequest;
import sst.auth.dto.LoginResponse;
import sst.auth.dto.SignUpRequest;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;
import sst.global.security.provider.JwtTokenProvider;
import sst.global.utils.CookieUtil;
import sst.member.domain.Member;
import sst.member.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final MemberMapper memberMapper;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieUtil cookieUtil;
	
	/**
	 * 회원 가입
	 * @param SignUpRequest 사용자 가입정보 
	 */
	@Transactional
	public Member addMember(SignUpRequest request) {
		// 이메일 중복 체크
		memberMapper.findMemberByEmail(request.getMbrEmail())
									        .ifPresent(member -> {
									            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
									        });
		
		Member member = Member.builder()
					  		  .mbrEmail(request.getMbrEmail())
			                  .mbrPassword(passwordEncoder.encode(request.getMbrPassword()))
			                  .mbrName(request.getMbrName())         
			                  .mbrNickname(request.getMbrNickname())
			                  .mbrTelno(request.getMbrTelno())       
			                  .mbrZip(request.getMbrZip())           
			                  .mbrAddr(request.getMbrAddr())         
			                  .mbrDaddr(request.getMbrDaddr())       
			                  .mbrProviderCd("LOCAL")
			                  .mbrAuthCd("ROLE_USER")    
			                  .mbrUseYn("Y")
			                  .build();
			  
		memberMapper.saveMember(member);
		
		return member;
	}
	
	/**
	 * 로그인
	 * @param LoginRequest 로그인 정보
	 */
	@Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {

		Member member = memberMapper.findMemberByEmail(request.getMbrEmail())
									.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
		// 비밀번호 검증 (입력값 평문 vs DB 암호화값 비교)
		if (!passwordEncoder.matches(request.getMbrPassword(), member.getMbrPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
		
		// Access Token + Refresh Token 발급 
		String accessToken  = jwtTokenProvider.createAccessToken(member.getMbrEmail(), member.getMbrAuthCd());
		String refreshToken = jwtTokenProvider.createRefreshToken(member.getMbrEmail());
		
		 // Refresh Token을 DB에 저장 
		memberMapper.updateRefreshTokenById(member.getMbrId(), refreshToken);
		
		
		// 로그인 성공 시 마지막 로그인 시간 업데이트
        memberMapper.updateLastLoginDate(member.getMbrId());
        
        // httpOnly 쿠키로 토큰을 브라우저에 전달
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessTokenCookie(accessToken).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshTokenCookie(refreshToken).toString());
        
        return LoginResponse.builder()
			                .mbrId(member.getMbrId())
			                .mbrEmail(member.getMbrEmail())
			                .mbrName(member.getMbrName())
			                .mbrNickname(member.getMbrNickname())
			                .memberRole(member.getMbrAuthCd()) 
			                .mbrProviderCd(member.getMbrProviderCd())
			                .mbrProfileInfo(member.getProfileFile())
			                .build();
    }
	
	/**
     * Access Token 재발급 (Refresh Token 검증 후)
     * @param request  Refresh Token 쿠키를 포함한 요청
     * @param response 새 Access Token 쿠키를 추가할 응답
     */
    public void refresh(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 Refresh Token 추출
        String refreshToken = cookieUtil.extractCookie(request, CookieUtil.REFRESH_TOKEN_COOKIE);
        if (refreshToken == null) {
        	throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // JWT 파싱으로 이메일 추출 (만료 시 ExpiredTokenException 발생)
        String email = jwtTokenProvider.getEmail(refreshToken);

        // DB에 저장된 RefreshToken과 비교 (탈취된 토큰 재사용 방지)
        Member member = memberMapper.findMemberByEmail(email)
        							.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        if (!refreshToken.equals(member.getMbrRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 새 Access Token 발급 → 쿠키로 전달
        String newAccessToken = jwtTokenProvider.createAccessToken(email, member.getMbrAuthCd());
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessTokenCookie(newAccessToken).toString());
    }
    
    /**
     * 로그아웃 — DB의 Refresh Token 삭제 + 쿠키 만료
     * @param email    로그아웃할 회원 이메일 (@AuthenticationPrincipal에서 추출)
     * @param id       로그아웃할 회원 ID
     * @param response 쿠키 삭제를 위한 응답 객체
     */
    @Transactional
    public void logout(Long memberId, HttpServletResponse response) {

        // DB에서 Refresh Token null로 업데이트 (무효화)
        memberMapper.updateRefreshTokenById(memberId, null);

        // Access Token/Refresh Token 쿠키 삭제
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.deleteAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.deleteRefreshTokenCookie().toString());

    }
    
    /**
     * 새로고침 시 자동 로그인 (Refresh Token 검증 -> Access Token 재발급 -> 사용자 정보 반환)
     * @param request  Refresh Token 쿠키를 포함한 요청
     * @param response 새 Access Token 쿠키를 추가할 응답
     * @return LoginResponse (사용자 정보)
     */
    @Transactional
    public LoginResponse verifyAndRefresh(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키에서 Refresh Token 추출
        String refreshToken = cookieUtil.extractCookie(request, CookieUtil.REFRESH_TOKEN_COOKIE);
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 2. JWT 파싱으로 이메일 추출 (만료 시 ExpiredTokenException 발생)
        String email = jwtTokenProvider.getEmail(refreshToken);

        // 3. DB에 저장된 RefreshToken과 비교
        Member member = memberMapper.findMemberByEmail(email)
                                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        if (!refreshToken.equals(member.getMbrRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 4. 새 Access Token 발급 → 쿠키로 전달
        String newAccessToken = jwtTokenProvider.createAccessToken(email, member.getMbrAuthCd());
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessTokenCookie(newAccessToken).toString());

        // 5. 프론트엔드 Context API 상태 유지를 위한 사용자 정보 반환
        return LoginResponse.builder()
			                .mbrId(member.getMbrId())
			                .mbrEmail(member.getMbrEmail())
			                .mbrName(member.getMbrName())
			                .mbrNickname(member.getMbrNickname())
			                .memberRole(member.getMbrAuthCd())
			                .mbrProviderCd(member.getMbrProviderCd())
			                .mbrProfileInfo(member.getProfileFile())	// 프로필 아이콘 파일 정보
			                .build();
    }
    
    // 닉네임 중복 여부
    @Transactional(readOnly = true)
    public boolean checkNicknameDuplicate(String nickname) {
        return memberMapper.existsByNickname(nickname) > 0;
    }
    
    // 이메일 중복 여부 반환 (true: 중복, false: 사용가능)
    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return memberMapper.existsByEmail(email) > 0;
    }
    
    
}







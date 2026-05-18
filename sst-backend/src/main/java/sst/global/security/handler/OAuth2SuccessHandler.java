// sst/auth/handler/OAuth2SuccessHandler.java
package sst.global.security.handler;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import sst.global.security.domain.CustomUserDetails;
import sst.global.security.provider.JwtTokenProvider;
import sst.global.utils.CookieUtil;
import sst.member.domain.Member;
import sst.member.mapper.MemberMapper;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final MemberMapper memberMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			Member member = userDetails.getMember();
			
			// 토큰 생성 (기존 로직과 동일)

			String accessToken = jwtTokenProvider.createAccessToken(member.getMbrEmail(), member.getMbrAuthCd());
	        String refreshToken = jwtTokenProvider.createRefreshToken(member.getMbrEmail());
	        
			// Refresh Token DB 업데이트
	        memberMapper.updateRefreshTokenById(member.getMbrId(), refreshToken);
	        
	        // 소셜 로그인 성공 시 마지막 로그인 시간 업데이트
            memberMapper.updateLastLoginDate(member.getMbrId());
            
			// HttpOnly 쿠키에 토큰 탑재 (기존 로직과 동일)
			response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(accessToken).toString());
			response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(refreshToken).toString());
			
			// 프론트엔드의 OAuth2 처리용 중간 정거장 컴포넌트로 리다이렉트
			String targetUrl = "http://localhost:5173/oauth/redirect";
	        getRedirectStrategy().sendRedirect(request, response, targetUrl);
	
    }
}
package sst.global.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import sst.global.security.provider.JwtTokenProvider;
import sst.global.security.service.CustomUserDetailsService;
import sst.global.utils.CookieUtil;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final CookieUtil cookieUtil;
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// httpOnly 쿠키에서 Access Token 추출
        String accessToken = cookieUtil.extractCookie(request, CookieUtil.ACCESS_TOKEN_COOKIE);

        // 토큰이 없거나 유효하지 않으면 다음 필터로 진행 (인증 없이 통과)
        if (!StringUtils.hasText(accessToken) || !jwtTokenProvider.validateToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 이미 인증된 요청이면 중복 처리 방지
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 이메일 추출 → DB에서 사용자 정보 조회
        String email = jwtTokenProvider.getEmail(accessToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        /**
         * SecurityContextHolder에 인증 객체 저장
         * UsernamePasswordAuthenticationToken: Spring Security 인증 토큰 객체
         */
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,        // principal (CustomUserDetails)
                        null,               // credentials (JWT 방식에서 불필요)
                        userDetails.getAuthorities()  // 권한 목록
                );

        // 요청 정보(IP 등)를 인증 객체에 추가
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContext에 인증 정보 저장 → 이후 @AuthenticationPrincipal로 꺼낼 수 있음
        SecurityContextHolder.getContext().setAuthentication(authentication);
		
		filterChain.doFilter(request, response);
		
	}
}

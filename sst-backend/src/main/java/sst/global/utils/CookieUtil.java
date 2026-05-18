package sst.global.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {

    // 운영시 true
    @Value("${cookie.secure}")
    private boolean secure;

    // Access Token 쿠키 이름
    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    // Refresh Token 쿠키 이름
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    // Access Token 만료 시간: 30분
    private static final int ACCESS_TOKEN_MAX_AGE = 1800;

    // Refresh Token 만료 시간: 1일
    private static final int REFRESH_TOKEN_MAX_AGE = 86400;

    /**
     * Access Token httpOnly 쿠키 생성
     * @param accessToken JWT Access Token 문자열
     * @return Set-Cookie 헤더에 추가할 ResponseCookie 객체
     */
    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, accessToken)
                .httpOnly(true)          // JavaScript에서 접근 불가 (XSS 방어)
                .secure(secure)          // HTTPS 환경에서만 전송
                .sameSite("Lax")         // CSRF 방어: 같은 사이트 + 안전한 cross-site 요청 허용
                .path("/")               // 모든 경로에서 쿠키 포함
                .maxAge(ACCESS_TOKEN_MAX_AGE)
                .build();
    }

    /**
     * Refresh Token httpOnly 쿠키 생성
     * Path로 제한하여 노출 방지  
     * @param refreshToken JWT Refresh Token 문자열
     * @return Set-Cookie 헤더에 추가할 ResponseCookie 객체
     */
    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)          // JavaScript에서 접근 불가 (XSS 방어)
                .secure(secure)          // HTTPS 환경에서만 전송
                .sameSite("Lax")         // CSRF 방어
                .path("/api/auth")       // 재발급·로그아웃 경로에서만 전송
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .build();
    }

    /**
     * Access Token 쿠키 삭제 (Max-Age=0)
     * @return 삭제용 Set-Cookie 헤더에 추가할 ResponseCookie 객체
     */
    public ResponseCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)               // 즉시 만료
                .build();
    }

    /**
     * Refresh Token 쿠키 삭제 (Max-Age=0)
     *
     * @return 삭제용 Set-Cookie 헤더에 추가할 ResponseCookie 객체
     */
    public ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(0)               // 즉시 만료
                .build();
    }

    /**
     * HttpServletRequest의 쿠키 배열에서 특정 쿠키 값을 추출.
     * @param request    HttpServletRequest
     * @param cookieName 찾을 쿠키 이름
     * @return 쿠키 값, 없으면 null
     */
    public String extractCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
